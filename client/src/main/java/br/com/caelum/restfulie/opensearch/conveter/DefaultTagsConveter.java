package br.com.caelum.restfulie.opensearch.conveter;

import br.com.caelum.restfulie.opensearch.Tags;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DefaultTagsConveter implements Converter {

	@SuppressWarnings("rawtypes") 
	public boolean canConvert(Class clazz) {
		return clazz.equals(Tags.class);
	}

	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		
		Tags tags = new Tags();
		String values = reader.getValue();
		
		if(values.equals("")) {
			return tags;
		}
		
		for (String value : values.trim().split("\\s+")) {
			tags.add(value);
		}
		return tags;
	}

}
