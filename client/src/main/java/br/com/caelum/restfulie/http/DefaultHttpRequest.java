package br.com.caelum.restfulie.http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.feature.FollowRedirects;
import br.com.caelum.restfulie.feature.RequestFeature;
import br.com.caelum.restfulie.feature.ResponseFeature;
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

	protected RequestStack stack;

    private final ExecutorService threads;

	public DefaultHttpRequest(URI uri, RestClient client) {
		this.uri = uri;
		this.client = client;
		this.stack = new RequestStack(client);
		this.threads = client.getThreads();
	}

	private Response sendPayload(Object payload, String verb) {
		RequestStack stackLocal = createStack();
		return stackLocal.process(this, verb, uri, payload);
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

    public Future<Response> getAsync(RequestCallback requestCallback) {
        return threads.submit(new AsynchronousRequest(this, HttpMethod.GET, requestCallback));
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

    public Future<Response> deleteAsync(RequestCallback requestCallback) {
        return threads.submit(new AsynchronousRequest(this, HttpMethod.DELETE, requestCallback));
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

    public <T> Future<Response> postAsync(T payload, RequestCallback requestCallback) {
        return threads.submit(new AsynchronousRequest(this, HttpMethod.POST, payload, requestCallback));
    }

	public <T> Response put(T object) {
		return sendPayload(object, "PUT");
	}

    public <T> Future<Response> putAsync(T payload, RequestCallback requestCallback) {
        return threads.submit(new AsynchronousRequest(this, HttpMethod.PUT, payload, requestCallback));
    }

	public Map<String, String> getHeaders() {
		return headers;
	}

	public Request addHeaders(Map<String, String> headers) {
		this.headers.putAll(headers);
		return this;
	}

	public Response access() {
		RequestStack stackLocal = createStack();
		return stackLocal.process(this, verb, uri, null);
	}

	/**
	 * override in order to provide your custom default stack
	 *
	 * @return
	 */
	protected RequestStack createStack() {
		stack.with(new FollowRedirects(client));
		return stack;
	}

	public Request handling(String type) {
		return as(type).accept(type);
	}

	public URI getURI() {
		return this.uri;
	}

	public Request with(RequestFeature feature) {
		this.stack.with(feature);
		return this;
	}

	public Request with(ResponseFeature feature) {
		return this;
	}

}
