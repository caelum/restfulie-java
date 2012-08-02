package br.com.caelum.restfulie.http.javanet;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.caelum.restfulie.Link;
import br.com.caelum.restfulie.http.Header;
import br.com.caelum.restfulie.http.Headers;

public class MapHeaders implements Headers{

	private final Map<String, List<String>> fields;

	public MapHeaders(Map<String, List<String>> fields) {
		this.fields = fields;
	}

	public List<String> get(String key) {
		List<String> values = fields.get(key); 
		if(values==null) values = fields.get(key.toLowerCase());
		return values;
	}

	public String getMain(String key) {
		if(!fields.containsKey(key)&&!fields.containsKey(key.toLowerCase())) {
			throw new IllegalArgumentException("Unable to parse as field does not exist.");
		}
		return get(key).get(0).split(";")[0];
	}

	public String getFirst(String key) {
		return get(key).get(0);
	}

	public List<Link> getLinks() {
		return null;
	}

	public Link getLink(String rel) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator<Header> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
