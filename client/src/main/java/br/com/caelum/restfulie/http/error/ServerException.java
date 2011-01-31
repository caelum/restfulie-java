package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 500, 502..599 response code
 * 
 * @author jose donizetti
 *
 */
public class ServerException extends RestfulieException {

	private static final long serialVersionUID = 6099610123350347391L;

	public ServerException(String message) {
		super(message);
	}


}
