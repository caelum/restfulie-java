package br.com.caelum.restfulie.http.apache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import br.com.caelum.restfulie.Link;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.http.DefaultRelation;
import br.com.caelum.restfulie.http.Headers;
import br.com.caelum.restfulie.http.SimpleHeader;

public class ApacheHeaders implements Headers {
	private final HttpResponse response;
	private List<Link> links;
	private final RestClient client;

	public ApacheHeaders(HttpResponse response, RestClient client) {
		this.response = response;
		this.client = client;
		this.links = new ArrayList<Link>();
	}

	public String getMain(String key) {
		return getFirst(key).split(";")[0];
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
		Header[] headers = response.getHeaders(key);
		return headers != null && headers.length > 0 ? get(key).get(0) : "";
	}

	public List<Link> links() {
		if(links.isEmpty()) {
			for ( String links : get("link")) {
				for(String link : links.split(",")){
					String[] split = link.split(";");
					String href = split[0].trim().substring(1,split[0].trim().length()-1);
					String rel = split[1].trim().substring(5,split[1].trim().length()-1);
					this.links.add(new DefaultRelation(rel,href,client));
				}
			}
		}
		return links;
	}

	public Link link(String rel) {
		links();
		for(Link link : links) {
			if(link.getRel().equals(rel)) {
				return link;
			}
		}
		return null;
	}

	public Iterator<br.com.caelum.restfulie.http.Header> iterator() {
		Header[] headers = response.getAllHeaders();
		List<br.com.caelum.restfulie.http.Header> list = new ArrayList<br.com.caelum.restfulie.http.Header>();
		for (Header header : headers) {
			list.add(new SimpleHeader(header.getName(), header.getValue()));
		}
		return list.iterator();
	}
}
