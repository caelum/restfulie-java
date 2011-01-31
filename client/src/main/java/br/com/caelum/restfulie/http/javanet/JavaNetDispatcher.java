package br.com.caelum.restfulie.http.javanet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.Map;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.RestfulieException;
import br.com.caelum.restfulie.http.ContentProcessor;
import br.com.caelum.restfulie.http.HttpURLConnectionContentProcessor;
import br.com.caelum.restfulie.http.IdentityContentProcessor;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.mediatype.MediaType;
import br.com.caelum.restfulie.request.RequestDispatcher;

public class JavaNetDispatcher implements RequestDispatcher {

	private final RestClient client;
	private String cookie;

	public JavaNetDispatcher(RestClient client) {
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
			String type = headers.get("Content-type");
			handlerFor(type).marshal(payload, writer, client);
			writer.flush();
			return responseFor(connection, new IdentityContentProcessor(), details);
		} catch (IOException e) {
			throw new RestfulieException("Unable to execute " + uri, e);
		}
	}

	private MediaType handlerFor(String type) {
		return client.getMediaTypes().forContentType(type);
	}

	private HttpURLConnection prepareConnectionWith(
			Map<String, String> headers, URI uri) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) uri.toURL()
				.openConnection();
		if (this.cookie != null) {
			connection.addRequestProperty("Cookie", this.cookie);
		}
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
			JavaNetResponse response = responseFor(connection,
					new HttpURLConnectionContentProcessor(connection), request);
			return response;
		} catch (IOException e) {
			throw new RestfulieException("Unable to execute " + uri, e);
		}
	}

	private JavaNetResponse responseFor(HttpURLConnection connection,
			ContentProcessor processor, Request request) throws IOException {
		JavaNetResponse response = new JavaNetResponse(connection, client,
				processor, request);
		extractCookie(response);
		return response;
	}

	private void extractCookie(JavaNetResponse response) {
		List<String> cookie = response.getHeader("Set-Cookie");
		
		if (cookie != null && cookie.size() > 0) {
			this.cookie = cookie.get(0);
			if (this.cookie.contains(";")) {
				this.cookie = this.cookie
						.substring(0, this.cookie.indexOf(";"));
			}
		}
	}

}
