package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 402, 406, 408, 411, 413..499 response code
 * 
 * @author jose donizetti
 *
 */
public class ClientException extends RestfulieException {

	private static final long serialVersionUID = 1388113693249430023L;

	public ClientException(String message) {
		super(message);
	}

}
