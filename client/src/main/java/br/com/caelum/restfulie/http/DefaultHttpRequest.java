package br.com.caelum.restfulie.http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.feature.FollowRedirects;
import br.com.caelum.restfulie.request.RequestStack;

/**
 * A default request builder implementation
 * @author guilherme silveira
 */
public class DefaultHttpRequest implements Request {
	
	private String verb = "GET";

	private final Map<String, String> headers = new HashMap<String, String>();

	private final URI uri;

	private final RestClient client;

	public DefaultHttpRequest(URI uri, RestClient client) {
		this.uri = uri;
		this.client = client;
	}

	private Response sendPayload(Object payload, String verb) {
		RequestStack stack = createStack();
		return stack.process(this, verb, uri, payload);
	}
	

	public Request with(String key, String value) {
		headers.put(key, value);
		return this;
	}

	public Request using(String verb) {
		this.verb = verb;
		return this;
	}

	public Request accept(String type) {
		return with("Accept", type);
	}

	public Response get() {
		return retrieve("GET");
	}

	private Response retrieve(String verb) {
		return using(verb).access();
	}

	public Request as(String contentType) {
		return with("Content-type", contentType);
	}

	public Response delete() {
		return retrieve("DELETE");
	}

	public Response head() {
		return retrieve("HEAD");
	}

	public Response options() {
		return retrieve("OPTIONS");
	}

	public <T> Response patch(T object) {
		return sendPayload(object, "PATCH");
	}

	public <T> Response post(T object) {
		return sendPayload(object, "POST");
	}

	public <T> Response put(T object) {
		return sendPayload(object, "PUT");
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public Response access() {
		RequestStack stack = createStack();
		return stack.process(this, verb, uri, null);
	}

	private RequestStack createStack() {
		RequestStack stack = new RequestStack(client);
//		stack.with(new CurlLogging());
		stack.with(new FollowRedirects(client));
		return stack;
	}

	public Request handling(String type) {
		return as(type).accept(type);
	}

	public String getHost() {
		return this.uri.getHost();
	}

}
