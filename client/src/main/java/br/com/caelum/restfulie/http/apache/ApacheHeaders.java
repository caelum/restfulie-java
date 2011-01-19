package br.com.caelum.restfulie.http.apache;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import br.com.caelum.restfulie.http.Headers;

public class ApacheHeaders implements Headers {
	private final HttpResponse response;

	public ApacheHeaders(HttpResponse response) {
		this.response = response;
	}

	public String getMain(String key) {
		return response.getHeaders(key)[0].getValue().split(";")[0];
	}

	public List<String> get(String key) {
		Header[] values = response.getHeaders(key);
		List<String> list = new ArrayList<String>();
		for(Header h : values) {
			list.add(h.getValue());
		}
		return list;
	}

	public String getFirst(String key) {
		return get(key).get(0);
	}

}
