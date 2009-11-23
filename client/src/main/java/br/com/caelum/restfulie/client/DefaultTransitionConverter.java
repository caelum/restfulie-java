package br.com.caelum.restfulie.client;

import br.com.caelum.restfulie.DefaultTransition;
import br.com.caelum.restfulie.XStreamDeserializer;
import br.com.caelum.restfulie.config.XStreamConfig;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Default transition converter.<br>
 * Injects the current deserializer to all transitions.
 * 
 * @author guilherme silveira
 * 
 */
public class DefaultTransitionConverter implements Converter {

	private final XStreamDeserializer deserializer;
	private final XStreamConfig config;

	public DefaultTransitionConverter(XStreamConfig config, XStreamDeserializer deserializer) {
		this.config = config;
		this.deserializer = deserializer;
	}

	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		// marshalling does not do anything
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		String rel = reader.getAttribute("rel");
		String href = reader.getAttribute("href");
		return new DefaultTransition(rel, href, deserializer, config);
	}

	public boolean canConvert(Class type) {
		return type.equals(DefaultTransition.class);
	}

}
