package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 412 response code
 * 
 * @author jose donizetti
 *
 */
public class PreconditionFailedException extends RestfulieException {

	private static final long serialVersionUID = 6515303038251543597L;

	public PreconditionFailedException(String message) {
		super(message);
	}

}
