package br.com.caelum.restfulie.feature;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.request.ResponseChain;
import br.com.caelum.restfulie.request.ResponseFeature;

public class RedirectAfterCreate implements ResponseFeature {

	public Response process(ResponseChain chain, Response response) {
		if (response.getCode() == 201) {
			return chain.getClient().at(response.getHeader("Location").get(0)).get();
		}
		return response;
	}

}
