package br.com.caelum.restfulie.http.javanet;

import java.util.List;
import java.util.Map;

import br.com.caelum.restfulie.http.Headers;

public class MapHeaders implements Headers{

	private final Map<String, List<String>> fields;

	public MapHeaders(Map<String, List<String>> fields) {
		this.fields = fields;
	}

	public List<String> get(String key) {
		return fields.get(key);
	}

	public String getMain(String key) {
		if(!fields.containsKey(key)) {
			throw new IllegalArgumentException("Unable to parse as field does not exist.");
		}
		return get(key).get(0).split(";")[0];
	}

	public String getFirst(String key) {
		return get(key).get(0);
	}

}
