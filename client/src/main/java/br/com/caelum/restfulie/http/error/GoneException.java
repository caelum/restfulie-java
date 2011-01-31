package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 410 response code
 * 
 * @author jose donizetti
 *
 */
public class GoneException extends RestfulieException {

	private static final long serialVersionUID = 4057096870697483501L;

	public GoneException(String message) {
		super(message);
	}

}
