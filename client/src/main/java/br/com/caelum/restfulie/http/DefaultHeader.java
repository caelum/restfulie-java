package br.com.caelum.restfulie.http;

public class DefaultHeader implements Header {

	private final String name;
	private final String value;

	public DefaultHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	
	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

}
