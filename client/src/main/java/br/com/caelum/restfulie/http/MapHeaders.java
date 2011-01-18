package br.com.caelum.restfulie.http;

import java.util.List;
import java.util.Map;

public class MapHeaders implements Headers{

	private final Map<String, List<String>> fields;

	public MapHeaders(Map<String, List<String>> fields) {
		this.fields = fields;
	}

	public List<String> getRaw(String key) {
		return fields.get(key);
	}

	public String getMain(String key) {
		if(!fields.containsKey(key)) {
			throw new IllegalArgumentException("Unable to parse as field does not exist.");
		}
		return getRaw(key).get(0).split(";")[0];
	}

}
