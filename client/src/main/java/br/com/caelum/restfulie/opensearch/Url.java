package br.com.caelum.restfulie.opensearch;

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

	public Url search(String term) {
		this.term = term;
		return this;
	}
	
	public Url atPage(int page) {
		this.page = page;
		return this;
	}

	public String getUri() {
		 String url = template.replace("{searchTerms}", term);  
		 url = url.replace("{startPage?}", page+"");  
		return url;
	}

}
	