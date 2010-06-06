package br.com.caelum.restfulie.http;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.restfulie.Resource;
import br.com.caelum.restfulie.client.DefaultTransitionConverter;

import com.thoughtworks.xstream.XStream;

/**
 * A default implemenation for xml media type based on XStream.<br/>
 * Extend it and override the getXStream method to configure the xstream instance with extra parameters.
 * 
 * @author guilherme silveira
 */
public class XmlMediaType implements MediaType {
	
	private final List<String> types = new ArrayList<String>();
	
	{
		types.add("application/xml");
		types.add("xml");
		types.add("text/xml");
	}
	
	@Override
	public boolean answersTo(String type) {
		return types.contains(type);
	}


	@Override
	public <T> void marshal(T payload, Writer writer) throws IOException {
		XStream xStream = getXStream();
		xStream.toXML(payload, writer);
		writer.flush();
	}


	@Override
	public Resource unmarshal(String content, MediaTypes types) {
		XStream xstream = getXStream();
		xstream.registerConverter(new DefaultTransitionConverter(types));
		return (Resource) xstream.fromXML(content);
	}

	protected XStream getXStream() {
		return new XStream();
	}


}
