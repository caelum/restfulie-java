package br.com.caelum.restfulie.opensearch;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenSearch tags element
 * @author jose donizetti
 */
public class Tags {
	private List<String> tags = new ArrayList<String>();

	public int size() {
		return tags.size();
	}

	public boolean add(String o) {
		return tags.add(o);
	}

	public String get(int index) {
		return tags.get(index);
	}

	
}
