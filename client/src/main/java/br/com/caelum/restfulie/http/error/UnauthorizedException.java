package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 401 response code
 * 
 * @author jose donizetti
 *
 */
public class UnauthorizedException extends RestfulieException {

	private static final long serialVersionUID = 2963522403558660414L;

	public UnauthorizedException(String message) {
		super(message);
	}


}
