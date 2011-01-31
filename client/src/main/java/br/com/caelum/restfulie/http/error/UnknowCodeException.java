package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * Unkown responde code
 * 
 * @author jose donizetti
 *
 */
public class UnknowCodeException extends RestfulieException {

	private static final long serialVersionUID = -8286986069712242964L;

	public UnknowCodeException(String message) {
		super(message);
	}


}
