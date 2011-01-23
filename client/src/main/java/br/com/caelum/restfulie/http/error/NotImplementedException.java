package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 501 response code
 * 
 * @author jose donizetti
 *
 */
public class NotImplementedException extends RestfulieException {

	private static final long serialVersionUID = 6469960679609931636L;

	public NotImplementedException(String message) {
		super(message);
	}

}
