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

}
