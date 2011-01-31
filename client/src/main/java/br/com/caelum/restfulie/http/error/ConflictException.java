package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 409 response code
 * 
 * @author jose donizetti
 *
 */
public class ConflictException extends RestfulieException {

	private static final long serialVersionUID = 2536017915960455465L;

	public ConflictException(String message) {
		super(message);
	}

}
