package br.com.caelum.restfulie.http;

public class SimpleHeader implements Header {

	private final String name;
	private final String value;

	public SimpleHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
