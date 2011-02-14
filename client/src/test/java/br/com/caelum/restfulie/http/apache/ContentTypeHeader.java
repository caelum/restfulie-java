package br.com.caelum.restfulie.http.apache;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

class ContentTypeHeader implements Header {
	
	private final String value;

	public ContentTypeHeader(String value) {
		this.value = value;
	}
	
	public String getValue() { return value; }
		
	public String getName() { return "Content-Type"; }
		
	public HeaderElement[] getElements() throws ParseException { return null; }
}