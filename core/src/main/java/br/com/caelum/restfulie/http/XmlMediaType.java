package br.com.caelum.restfulie.http;

import java.io.Writer;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.XStreamDeserializer;
import br.com.caelum.restfulie.serializer.BasicSerializer;
import br.com.caelum.restfulie.serializer.XStreamXmlSerializer;

public class XmlMediaType implements MediaType {
	
	private final List<String> types = new ArrayList<String>();
	
	{
		types.add("application/xml");
		types.add("xml");
		types.add("text/xml");
	}
	
	private BasicSerializer getSerializer(Writer writer) {
		return new XStreamXmlSerializer(config.create(), writer)
				.from(customObject);
	}

	
	private XStreamDeserializer getDeserializer() {
		return new XStreamDeserializer(this.config);
	}

	public void unmarshal(Request request, Response response) {
		
	}
	
	public void marshal(Object object, Writer writer) {
		BasicSerializer serializer = getSerializer(writer);
		serializer.serialize();
		writer.flush();
		
	}

	@Override
	public boolean answersTo(String type) {
		return types.contains(type);
	}



}
