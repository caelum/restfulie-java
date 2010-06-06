package br.com.caelum.restfulie.http;

import java.net.URI;

/**
 * An http client request provider.<br/>
 * Implementations provide access to requests over http.
 * 
 * @author guilherme silveira
 */
public interface HttpClientProvider {

	Request request(URI uri);

}
