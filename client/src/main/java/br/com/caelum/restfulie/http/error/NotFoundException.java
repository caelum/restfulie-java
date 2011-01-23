package br.com.caelum.restfulie.http.error;

import br.com.caelum.restfulie.RestfulieException;

/**
 * 404 response code
 * 
 * @author jose donizetti
 *
 */
public class NotFoundException extends RestfulieException {

	private static final long serialVersionUID = 4646962023618885027L;

	public NotFoundException(String message) {
		super(message);
	}


}
