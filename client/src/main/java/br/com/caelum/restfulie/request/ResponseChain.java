package br.com.caelum.restfulie.request;

import java.util.Iterator;
import java.util.List;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;

public class ResponseChain {

	private final Iterator<ResponseFeature> current;
	private final RestClient client;

	public ResponseChain(List<ResponseFeature> features, RestClient client) {
		this.client = client;
		this.current = features.iterator();
	}

	public Response next(Response response) {
		if(current.hasNext()) {
			return current.next().process(this, response);
		}
		return response;
	}

	public RestClient getClient() {
		return client;
	}

}
