package br.com.caelum.restfulie.feature;

import java.net.URI;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.request.RequestChain;

public class RetryWhenUnavailable implements RequestFeature {

	private Response response;

	public Response process(RequestChain chain, Request request, String verb, URI uri, Object payload) {
		
		response = chain.next(request, verb, uri, payload);
		if(shouldRetry()) {
			response = chain.next(request, verb, uri, payload);
		}
		
		return response;
	}
	
	protected boolean shouldRetry() {
		return response.getCode() == 503;
	}

}
