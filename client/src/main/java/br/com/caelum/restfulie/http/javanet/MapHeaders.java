package br.com.caelum.restfulie.http.javanet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.caelum.restfulie.Link;
import br.com.caelum.restfulie.http.Header;
import br.com.caelum.restfulie.http.Headers;
import br.com.caelum.restfulie.http.SimpleHeader;

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

	public List<Link> links() {
		return null;
	}

	public Link link(String rel) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator<Header> iterator() {
		List<Header> headers = new ArrayList<Header>();
		for (Entry<String, List<String>> header : fields.entrySet()) {
			List<String> values = header.getValue();
			for (String value : values) {
				headers.add(new SimpleHeader(header.getKey(), value));
			}
		}
		return headers.iterator();
	}
}
