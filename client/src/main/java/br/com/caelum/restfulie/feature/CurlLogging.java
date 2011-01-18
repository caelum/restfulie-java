package br.com.caelum.restfulie.feature;

import java.net.URI;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.request.RequestChain;

public class CurlLogging implements RequestFeature {

	public Response process(RequestChain chain, Request request, String verb,
			URI uri, Object payload) {
		if(verb.equals("POST")) {
			System.out.println(String.format("curl -v %s -H 'Content-type: %s' -d '%s'", uri, request.getHeaders().get("Content-type"), payload));
		} else {
			System.out.println(String.format("curl -v %s", uri));
		}
		return chain.next(request, verb, uri, payload);
	}

}
