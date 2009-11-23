package br.com.caelum.restfulie;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import br.com.caelum.restfulie.config.Configuration;
import br.com.caelum.restfulie.config.SimpleConfiguration;
import br.com.caelum.restfulie.marshall.ResourceSerializer;

/**
 * Configured service entry point.
 * 
 * @author guilherme silveira
 */
public class DefaultResources implements Resources {

	private final Map<Class, Configuration> configurations = new HashMap<Class, Configuration>();

	public Configuration configure(Class type) {
		Configuration config = new SimpleConfiguration(type);
		this.configurations.put(type, config);
		return config;
	}

	public ResourceSerializer entryAt(URI uri) {
		return new EntryPointService(uri, configurations);
	}

	public ResourceSerializer entryAt(String uri) throws URISyntaxException {
		return entryAt(new URI(uri));
	}

}
