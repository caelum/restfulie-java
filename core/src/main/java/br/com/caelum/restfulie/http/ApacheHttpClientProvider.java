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
	 */
	@Override
	public Request request(URI uri) {
		return new ApacheHttpRequest(uri);
	}

}
