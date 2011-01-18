package br.com.caelum.restfulie.request;

import br.com.caelum.restfulie.Response;

public interface ResponseFeature {

	Response process(ResponseChain responseChain, Response response);

}
