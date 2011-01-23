package br.com.caelum.restfulie.feature;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.http.error.BadRequestException;
import br.com.caelum.restfulie.http.error.RedicetionException;
import br.com.caelum.restfulie.request.ResponseChain;
import br.com.caelum.restfulie.request.ResponseFeature;

public class ThrowError implements ResponseFeature {

	public ThrowError(RestClient client) {
	}

	public Response process(ResponseChain responseChain, Response response) {
		int code = response.getCode();

		
		//300 range
		if(code >= 300 && code <= 399) {
			throw new RedicetionException("Http erro when invoking blah");
		}
		
		if(code == 400) {
			throw new BadRequestException("Http erro when invoking blah");
		}

		return null;
	}

}
