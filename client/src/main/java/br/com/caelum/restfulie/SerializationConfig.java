package br.com.caelum.restfulie;

import java.util.Collection;
import java.util.Map;

public class SerializationConfig {

	private final Map<Class, Configuration> configs;

	public SerializationConfig(Map<Class, Configuration> configs) {
		this.configs = configs;
	}

	public Configuration type(Class<? extends Object> type) {
		if(!configs.containsKey(type)) {
			configs.put(type, new SimpleConfiguration(type));
		}
		return configs.get(type);
	}

	public Collection<Configuration> getAllTypes() {
		return configs.values();
	}

}
