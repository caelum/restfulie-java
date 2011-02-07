package br.com.caelum.restfulie.feature;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.request.ResponseChain;

public interface ResponseFeature {

	Response process(ResponseChain responseChain, Response response);

}
