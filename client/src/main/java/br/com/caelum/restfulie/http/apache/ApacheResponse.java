package br.com.caelum.restfulie.http.apache;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.http.Headers;

public class ApacheResponse implements Response {

	private final HttpResponse response;
	private final RestClient client;
	private HttpEntity entity;

	public ApacheResponse(HttpResponse response, RestClient client) {
		this.response = response;
		this.client = client;
		this.entity = response.getEntity();
	}

	public int getCode() {
		return response.getStatusLine().getStatusCode();
	}

	public String getContent() throws IOException {
		if (entity == null) {
			return "";
		}
	    long len = entity.getContentLength();
			if (len != -1 && len < 10 * 1024 * 1024) {
			    return EntityUtils.toString(entity);
			} else {
			    return "";
			}
	}

	public List<String> getHeader(String key) {
		return getHeaders().getRaw(key);
	}

	public <T> T getResource() throws IOException {
		String contentType = getContentType();
		String content = getContent();
		return (T) client.getMediaTypes().forContentType(contentType).unmarshal(content, client);
	}

	private String getContentType() throws IOException {
		return getHeader("Content-Type").get(0).split(";")[0];
	}

	public Headers getHeaders() {
		return new ApacheHeaders(response);
	}

	public void discard() throws IOException {
		response.getEntity().consumeContent();
	}

}
