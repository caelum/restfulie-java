package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 300.399 response code
 * 
 * @author jose donizetti
 *
 */
public class RedicetionException extends RestfulieException {
	
	private static final long serialVersionUID = 3063468545997174918L;
	
	public RedicetionException(String message) {
		super(message);
	}
}
