package br.com.caelum.restfulie.http.apache;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.RestfulieException;
import br.com.caelum.restfulie.http.ContentProcessor;
import br.com.caelum.restfulie.http.HttpURLConnectionContentProcessor;
import br.com.caelum.restfulie.http.IdentityContentProcessor;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.request.RequestExecutor;

public class ApacheRequestExecutor implements RequestExecutor {

	private final RestClient client;

	public ApacheRequestExecutor(RestClient client) {
		this.client = client;
	}

	public Response process(Request details, String verb, URI uri,
			Object payload) {
		if (payload == null) {
			return access(details, verb, uri);
		}
		try {
			Map<String, String> headers = details.getHeaders();

			HttpURLConnection connection = prepareConnectionWith(headers, uri);
			if (!headers.containsKey("Content-type")) {
				throw new RestfulieException(
						"You should set a content type prior to sending some payload.");
			}
			connection.setDoOutput(true);
			connection.setRequestMethod(verb);
			OutputStream output = connection.getOutputStream();
			Writer writer = new OutputStreamWriter(output);
			client.getMediaTypes().forContentType(headers.get("Content-type"))
					.marshal(payload, writer);
			return responseFor(connection, new IdentityContentProcessor());
		} catch (IOException e) {
			throw new RestfulieException("Unable to execute " + uri, e);
		}
	}

	private HttpURLConnection prepareConnectionWith(
			Map<String, String> headers, URI uri) throws IOException,
			MalformedURLException {
		HttpURLConnection connection = (HttpURLConnection) uri.toURL()
				.openConnection();
		for (String header : headers.keySet()) {
			connection.addRequestProperty(header, headers.get(header));
		}
		return connection;
	}

	private Response access(Request request, String verb, URI uri) {
		try {
			HttpURLConnection connection = prepareConnectionWith(
					request.getHeaders(), uri);
			connection.setDoOutput(false);
			connection.setRequestMethod(verb);
			ApacheResponse response = responseFor(connection,
					new HttpURLConnectionContentProcessor(connection));
			return response;
		} catch (IOException e) {
			throw new RestfulieException("Unable to execute " + uri, e);
		}
	}

	private ApacheResponse responseFor(HttpURLConnection connection,
			ContentProcessor processor) throws IOException {
		return new ApacheResponse(connection, client, processor);
	}

}
