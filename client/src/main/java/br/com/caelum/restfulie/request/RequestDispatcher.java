package br.com.caelum.restfulie.request;

import java.net.URI;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.http.Request;

/**
 * A dispatcher that will actually execute the request.
 * 
 * @author Guilherme Silveira
 */
public interface RequestDispatcher {

	Response process(Request request, String verb, URI uri, Object payload);

}
