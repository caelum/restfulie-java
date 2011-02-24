package br.com.caelum.restfulie.opensearch.conveter;

import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.opensearch.Url;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DefaultUrlConverter implements Converter {

	private final RestClient client;

	public DefaultUrlConverter(RestClient client) {
		this.client = client;
	}

	public boolean canConvert(Class clazz) {
		return clazz.equals(Url.class);
	}

	public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {

	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String type = reader.getAttribute("type");
		String template = reader.getAttribute("template");
		
		return new Url(type,template,client);
	}

}
