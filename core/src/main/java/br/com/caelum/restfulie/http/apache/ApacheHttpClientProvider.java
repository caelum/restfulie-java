package br.com.caelum.restfulie.http.apache;

import java.net.URI;

import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.http.HttpClientProvider;
import br.com.caelum.restfulie.http.Request;

/**
 * An apache http based request provider.
 * 
 * @author guilherme silveira
 */
public class ApacheHttpClientProvider implements HttpClientProvider {
	
	/**
	 * Provides a request for a specific uri basedf on apache http.
	 * @param types 
	 */
	@Override
	public Request request(URI uri, RestClient client) {
		return new ApacheHttpRequest(uri, client);
	}

}
