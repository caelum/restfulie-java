package br.com.caelum.restfulie.vraptor;

import java.io.Writer;

import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.serialization.XStreamXMLSerializer;

import com.thoughtworks.xstream.XStream;

public class LinksSerializer implements Serializer {
	
	private final XStreamXMLSerializer serializer;

	public LinksSerializer(XStream xstream, Writer writer, TypeNameExtractor extractor) {
		this.serializer = new XStreamXMLSerializer(xstream, writer, extractor);
	}

	public Serializer exclude(String... arg0) {
		return serializer.exclude(arg0);
	}

	public <T> Serializer from(T object) {
		return serializer.from(object);
	}

	public Serializer include(String... arg0) {
		return serializer.include(arg0);
	}

	public void serialize() {
		serializer.serialize();
	}

}
