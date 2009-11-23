package br.com.caelum.restfulie;

import java.net.URI;
import java.net.URISyntaxException;

import br.com.caelum.restfulie.config.Configuration;
import br.com.caelum.restfulie.marshall.ResourceSerializer;

/**
 * Allows resources configuration and access.
 * 
 * @author guilherme silveira
 */
public interface Resources {

	/**
	 * Configures an specific type
	 */
	Configuration configure(Class type);

	/**
	 * Gives access to a system's entry point.
	 */
	ResourceSerializer entryAt(URI uri);

	/**
	 * Gives access to a system's entry point.
	 * @throws URISyntaxException 
	 */
	ResourceSerializer entryAt(String uri) throws URISyntaxException;
}