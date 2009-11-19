package br.com.caelum.restfulie.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles a set of configurations.
 * 
 * @author guilherme silveira
 */
public class SerializationConfig {

	private final Map<Class, Configuration> configs;
	
	public SerializationConfig(Map<Class, Configuration> configs) {
		this.configs = configs;
	}

	public SerializationConfig() {
		this(new HashMap<Class,Configuration>());
	}

	public Configuration type(Class<? extends Object> type) {
		if (!configs.containsKey(type)) {
			configs.put(type, new SimpleConfiguration(type));
		}
		return configs.get(type);
	}

	public Collection<Configuration> getAllTypes() {
		return configs.values();
	}

}
