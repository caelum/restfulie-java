package br.com.caelum.restfulie.http;

import java.net.URI;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.request.RequestDispatcher;

/**
 * An http client request provider.<br/>
 * Implementations provide access to requests over http.
 * 
 * @author guilherme silveira
 */
public interface HttpClientProvider {

	Response process(Request request, String verb, URI uri, Object payload);

}
