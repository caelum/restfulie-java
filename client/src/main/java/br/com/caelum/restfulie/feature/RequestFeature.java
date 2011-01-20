package br.com.caelum.restfulie.feature;

import java.net.URI;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.request.RequestChain;

/**
 * A request feature is a interceptor that will be invoked prior to the
 * execution of the request. Compose http behavior by using your own features in the feature stack.
 * 
 * @author guilherme silveira
 */
public interface RequestFeature {

	Response process(RequestChain chain, Request request, String verb, URI uri,
			Object payload);

}
