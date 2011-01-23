package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 405 response code
 * 
 * @author jose donizetti
 *
 */
public class MethodNotAllowedException extends RestfulieException{

	private static final long serialVersionUID = 3491771205478082847L;

	public MethodNotAllowedException(String message) {
		super(message);
	}


}
