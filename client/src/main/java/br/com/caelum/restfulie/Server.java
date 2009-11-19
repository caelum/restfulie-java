package br.com.caelum.restfulie;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import br.com.caelum.restfulie.config.Configuration;
import br.com.caelum.restfulie.config.SimpleConfiguration;

public class Server {
	
	public static Server server() {
		return new Server();
	}

	private final Map<Class, Configuration> configurations = new HashMap<Class, Configuration>();
	
	public Configuration configure(Class type) {
		Configuration config = new SimpleConfiguration(type);
		this.configurations.put(type,config);
		return config;
	}
	
	public EntryPointService service(URI uri) {
		return new EntryPointService(uri, configurations);
	}

}
