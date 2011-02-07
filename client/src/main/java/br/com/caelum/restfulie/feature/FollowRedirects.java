package br.com.caelum.restfulie.feature;

import java.net.URI;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.request.ResponseChain;

/**
 * A feature that automatically follows 300~399 and 201 codes by using the
 * Location header.
 *
 * @author guilherme silveira
 */
public class FollowRedirects implements ResponseFeature {

	private final RestClient client;

	public FollowRedirects(RestClient client) {
		this.client = client;
	}

	public Response process(ResponseChain chain, Response response) {
		if (shouldRedirect(response)) {
			String uri = response.getHeader("Location").get(0);
			Request request = response.getRequest();
			if (uri.charAt(0) == '/') {
				URI target = request.getURI().resolve(uri);
				return client.at(target).addHeaders(request.getHeaders()).get();
			}
			return client.at(uri).addHeaders(request.getHeaders()).get();
		}
		return response;
	}

	/**
	 * Extension point that redirects 201 and 3XX. Overwrite to redirect only
	 * the ones you want.
	 */
	protected boolean shouldRedirect(Response response) {
		return response.getCode() / 100 == 3 || response.getCode() == 201;
	}

}
