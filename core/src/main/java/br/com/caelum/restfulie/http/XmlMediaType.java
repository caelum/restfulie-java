package br.com.caelum.restfulie.http;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import br.com.caelum.restfulie.DefaultRelation;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.client.DefaultTransitionConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * A default implemenation for xml media type based on XStream.<br/>
 * Extend it and override the getXStream method to configure the xstream instance with extra parameters.
 * 
 * @author guilherme silveira
 */
@SuppressWarnings("unchecked")
public class XmlMediaType implements MediaType {
	
	private final List<String> types = Arrays.asList("application/xml", "text/xml", "xml");
	
	private final XStreamHelper helper;

	private final XStream xstream;
	
	public XmlMediaType() {
		QNameMap qnameMap = new QNameMap();
		QName qname = new QName("http://www.w3.org/2005/Atom", "atom");
		qnameMap.registerMapping(qname, DefaultRelation.class);
		helper = new XStreamHelper(new StaxDriver(qnameMap));
		this.xstream = helper.getXStream(getTypesToEnhance());
		configure(xstream);
	}

	/**
	 * Allows xstream further configuration.
	 */
	protected void configure(XStream xstream) {
	}

	@Override
	public boolean answersTo(String type) {
		return types.contains(type);
	}

	@Override
	public <T> void marshal(T payload, Writer writer) throws IOException {
		xstream.toXML(payload, writer);
		writer.flush();
	}

	@Override
	public <T> T unmarshal(String content, RestClient client) {
		xstream.registerConverter(new DefaultTransitionConverter(client));
		return (T) xstream.fromXML(content);
	}

	protected List<Class> getTypesToEnhance() {
		return new ArrayList<Class>();
	}


}
