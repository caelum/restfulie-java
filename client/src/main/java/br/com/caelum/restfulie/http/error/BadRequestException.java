package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 400 response code
 * 
 * @author jose donizetti
 *
 */
public class BadRequestException extends RestfulieException {

	private static final long serialVersionUID = 2258661686367560426L;

	public BadRequestException(String message) {
		super(message);
	}

}
