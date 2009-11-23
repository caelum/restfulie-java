package br.com.caelum.restfulie;

import java.net.URI;
import java.net.URISyntaxException;

import br.com.caelum.restfulie.marshall.ResourceSerializer;

/**
 * Restfulie's client API entry point.<br/>

 * @author guilherme silveira
 */
public class Restfulie {

	/**
	 * Given an retrieved resource, gives access to restfulie's transition api.
	 */
	public static <T> Resource resource(T object) {
		return (Resource) object;
	}

	/**
	 * Entry point to configure serialization data prior to accessing the resources.
	 */
	public static Resources resources() {
		return new DefaultResources();
	}

	/**
	 * Entry point to direct access an uri.
	 */
	public static ResourceSerializer resource(URI uri) {
		return new EntryPointService(uri);
	}

	/**
	 * Entry point to direct access an uri.
	 * @throws URISyntaxException 
	 */
	public static ResourceSerializer resource(String uri) throws URISyntaxException {
		return new EntryPointService(new URI(uri));
	}

}
