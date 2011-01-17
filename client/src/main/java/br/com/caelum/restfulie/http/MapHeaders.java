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

	public String getMain(String string) {
		if(!fields.containsKey("Content-Type")) {
			throw new IllegalArgumentException("Unable to unmarshall as there is no content type set. Check your server.");
		}
		return getRaw("Content-Type").get(0).split(";")[0];
	}

}
