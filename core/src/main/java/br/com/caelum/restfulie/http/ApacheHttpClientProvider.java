package br.com.caelum.restfulie.http;

import java.net.URI;

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
	public Request request(URI uri, MediaTypes types) {
		return new ApacheHttpRequest(uri, types);
	}

}
