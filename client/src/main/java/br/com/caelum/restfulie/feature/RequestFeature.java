package br.com.caelum.restfulie.feature;

import java.net.URI;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.request.RequestChain;

public interface RequestFeature {

	Response process(RequestChain chain, Request request, String verb, URI uri,
			Object payload);

}
