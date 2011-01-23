package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 403 response code
 * 
 * @author jose donizetti
 *
 */
public class ForbiddenException extends RestfulieException {

	private static final long serialVersionUID = 8835699291841679562L;

	public ForbiddenException(String message) {
		super(message);
	}


}
