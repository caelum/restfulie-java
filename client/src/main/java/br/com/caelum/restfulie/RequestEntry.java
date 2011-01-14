package br.com.caelum.restfulie;

import java.net.URI;
import java.net.URISyntaxException;

import br.com.caelum.restfulie.http.Request;

/**
 * Defines methods to access a rest system entry point.
 * 
 * @author guilherme silveira
 */
public interface RequestEntry {

	/**
	 * Entry point to direct access an uri.
	 */
	Request at(URI uri);

	/**
	 * Entry point to direct access an uri.
	 */
	Request at(String uri) throws RestfulieException ;

}
