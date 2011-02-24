package br.com.caelum.restfulie.opensearch;

import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.http.Request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * OpenSearch URL representation
 * @author jose donizetti
 *
 */
@XStreamAlias("Url")
public class Url {

	@XStreamAlias("type")
	@XStreamAsAttribute
	private String type;
	
	@XStreamAsAttribute
	@XStreamAlias("template")
	private String template;
	
	private int page;
	private String term = "";
	
	private final RestClient client;

	public Url(String type, String template, RestClient client) {
		this.type = type;
		this.template = template;
		this.client = client;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String toUri() {
		 String url = template.replace("{searchTerms}", term);  
		 url = url.replace("{startPage?}", page+"");  
		return url;
	}

	
	public static String queryFor(String query) {
		return query;
	}
	
	public static Integer page(Integer page) {
		return page;
	}

	public Url with(String queryFor) {
		this.term = queryFor;
		return this;
	}

	public Request and(Integer page) {
		this.page = page;
		return client.at(toUri());
	}
}
	