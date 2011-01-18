package br.com.caelum.restfulie.request;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.feature.RequestFeature;
import br.com.caelum.restfulie.http.Request;

public class RequestStack implements RequestFeature {
	
	private final List<ResponseFeature> responses = new ArrayList<ResponseFeature>();
	private final List<RequestFeature> requests = new ArrayList<RequestFeature>();
	private final RequestExecutor executor;
	private final RestClient client;
	
	public RequestStack(RequestExecutor executor, RestClient client) {
		this.executor = executor;
		this.client = client;
	}
	
	public void with(ResponseFeature feature) {
		this.responses.add(feature);
	}

	public Response process(Request request, String verb, URI uri, Object payload) {
		requests.add(this);
		return new RequestChain(requests).next(request, verb, uri, payload);
	}

	public void with(RequestFeature feature) {
		this.requests.add(feature);
	}

	public Response process(RequestChain chain, Request request, String verb,
			URI uri, Object payload) {
		Response response = executor.process(request, verb, uri, payload);
		return new ResponseChain(responses, client).next(response);
	}

}
