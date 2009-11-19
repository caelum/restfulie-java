package br.com.caelum.restfulie.config;


import com.thoughtworks.xstream.XStream;

public class XStreamConfig {

	private final SerializationConfig configs;

	public XStreamConfig(SerializationConfig configs) {
		this.configs = configs;
	}


	public void applyTo(XStream xstream) {
		for(Configuration config : configs.getAllTypes()) {
			xstream.processAnnotations(config.getType());
		}
	}

}
