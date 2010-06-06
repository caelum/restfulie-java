package br.com.caelum.restfulie.mediatype;

import java.util.Map;

import br.com.caelum.restfulie.Restfulie;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class MustIgnoreConverter implements Converter {

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		String property = reader.underlyingReader().getNodeName();
		String value = reader.underlyingReader().getValue();
		Object object = context.currentObject();
		Map<String, String> properties = Restfulie.resource(object).getUnknownProperties();
		properties.put(reader.getValue(), reader.getValue());
		return properties;
	}

	@Override
	public boolean canConvert(Class type) {
		return type.equals(MustIgnoreProperty.class);
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1,
			MarshallingContext arg2) {
	}

}
