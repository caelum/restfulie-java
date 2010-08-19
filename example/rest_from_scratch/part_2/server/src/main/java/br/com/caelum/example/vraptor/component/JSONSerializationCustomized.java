package br.com.caelum.example.vraptor.component;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.interceptor.DefaultTypeNameExtractor;
import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.serialization.ProxyInitializer;
import br.com.caelum.vraptor.serialization.xstream.XStreamJSONSerialization;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

@Component
public class JSONSerializationCustomized extends XStreamJSONSerialization {

	public JSONSerializationCustomized(HttpServletResponse response,
			TypeNameExtractor extractor, ProxyInitializer initializer) {
		super(response, extractor, initializer);
	}

	@Override
	protected XStream getXStream() {
		XStream xstream = super.getXStream();
		MyCollectionConverter converter = new MyCollectionConverter(xstream
				.getMapper());
		xstream.registerConverter(converter);
		return xstream;
	}

	public static void main(String[] args) {
		JSONSerializationCustomized ser = new JSONSerializationCustomized(null, new DefaultTypeNameExtractor(), null);
		XStream xstream = ser.getXStream();
		List l = new ArrayList();
		l.add(new Item("a", "b"));
		l.add(new Item("d", "c"));
		System.out.println(xstream.toXML(l));
		List l2 = (List) xstream.fromXML(xstream.toXML(l));
		System.out.println(l2);
	}

}

class Item {
	public String name;
	public String age;
	public Item(String name, String age) {
		super();
		this.name = name;
		this.age = age;
	}
	
}

class MyCollectionConverter extends CollectionConverter {
	public MyCollectionConverter(Mapper mapper) {
		super(mapper);
	}

	protected void writeItem(Object item, MarshallingContext context,
			HierarchicalStreamWriter writer) {
		// PUBLISHED API METHOD! If changing signature, ensure backwards
		// compatability.
		if (item == null) {
			super.writeItem(item, context, writer);
		} else {
			String name = mapper().serializedClass(item.getClass());
			writer.startNode(name);
			super.writeItem(item, context, writer);
			writer.endNode();
		}
	}

}