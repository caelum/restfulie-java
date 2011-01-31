package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 407 response code
 * 
 * @author jose donizetti
 *
 */
public class ProxyAuthenticationRequiredException extends RestfulieException {
	
	private static final long serialVersionUID = -5960166008854619628L;

	public ProxyAuthenticationRequiredException(String message) {
		super(message);
	}



}
