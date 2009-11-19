package br.com.caelum.restfulie;

import br.com.caelum.restfulie.client.DefaultTransitionConverter;
import br.com.caelum.restfulie.config.XStreamConfig;
import br.com.caelum.restfulie.unmarshall.Deserializer;

import com.thoughtworks.xstream.XStream;

/**
 * Deserialization support through xstream.
 * 
 * @author guilherme silveira
 * @author lucas souza
 * 
 */
public class XStreamDeserializer implements Deserializer {

	private final XStream xstream;
	
	public XStreamDeserializer(XStreamConfig config) {
		this.xstream = config.create();
		this.xstream.registerConverter(new DefaultTransitionConverter(this));
	}

	public XStreamDeserializer(XStream xStream) {
		this.xstream = xStream;
		this.xstream.registerConverter(new DefaultTransitionConverter(this));
	}

	public Object fromXml(String xml) {
		return xstream.fromXML(xml);
	}
	
}
