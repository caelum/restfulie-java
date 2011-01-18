package br.com.caelum.restfulie.feature;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.request.ResponseChain;
import br.com.caelum.restfulie.request.ResponseFeature;

public class FollowRedirects implements ResponseFeature {

	public Response process(ResponseChain chain, Response response) {
		if (response.getCode() / 100 == 3) {
			return chain.getClient().at(response.getHeader("Location").get(0)).get();
		}
		return response;
	}

}
