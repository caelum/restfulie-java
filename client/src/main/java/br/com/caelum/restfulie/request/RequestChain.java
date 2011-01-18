package br.com.caelum.restfulie.request;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.feature.RequestFeature;
import br.com.caelum.restfulie.http.Request;

public class RequestChain {

	private final Iterator<RequestFeature> current;
	public RequestChain(List<RequestFeature> requests) {
		this.current = requests.iterator();
	}

	public Response next(Request request, String verb, URI uri, Object payload) {
		if(current.hasNext()) {
			return current.next().process(this, request, verb, uri, payload); 
		}
		return null;
	}

}
