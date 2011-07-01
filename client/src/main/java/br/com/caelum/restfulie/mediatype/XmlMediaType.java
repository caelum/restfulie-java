package br.com.caelum.restfulie.mediatype;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.jvnet.inflector.Noun;

import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.client.DefaultLinkConverter;
import br.com.caelum.restfulie.http.DefaultRelation;
import br.com.caelum.restfulie.opensearch.conveter.DefaultUrlConverter;
import br.com.caelum.restfulie.relation.CachedEnhancer;
import br.com.caelum.restfulie.relation.DefaultEnhancer;
import br.com.caelum.restfulie.relation.Enhancer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

/**
 * A default implemenation for xml media type based on XStream.<br/>
 * Extend it and override the getXStream method to configure the xstream
 * instance with extra parameters.
 * 
 * @author guilherme silveira
 */
@SuppressWarnings("unchecked")
public class XmlMediaType implements MediaType {

	private final List<String> types = Arrays.asList("application/xml", "text/xml", "xml",
			"application/opensearchdescription+xml");

	private final XStreamHelper helper;

	private XStream xstream;

	private List<Class> typesToEnhance = new ArrayList<Class>();

	private List<String> names = new ArrayList<String>();

	public XmlMediaType(Enhancer enhancer) {
		QNameMap qnameMap = new QNameMap();
		QName qname = new QName("http://www.w3.org/2005/Atom", "atom");
		qnameMap.registerMapping(qname, DefaultRelation.class);
		// we need the replacer because
		// xstream replaces an _ with __ (two underscore) more information at
		// http://xstream.codehaus.org/faq.html#XML_double_underscores
		XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("$", "_");
		helper = new XStreamHelper(new StaxDriver(qnameMap, replacer), enhancer);
	}
	
	public XmlMediaType() {
		this(new CachedEnhancer(new DefaultEnhancer()));
	}

	/**
	 * Allows xstream further configuration.
	 */
	protected void configure(XStream xstream) {
	}

	public boolean answersTo(String type) {
		return types.contains(type);
	}

	public <T> void marshal(T payload, Writer writer, RestClient client) throws IOException {
		getXstream(client).toXML(getPayload(payload), writer);
		writer.flush();
	}

	private Object getPayload(Object payload) {
		if (payload instanceof Collection) {
			return new ArrayList((Collection) payload);
		} else {
			return payload;
		}
	}

	public <T> T unmarshal(String content, RestClient client) {
		XStream xstream = getXstream(client);
		xstream.registerConverter(new DefaultLinkConverter(client));
		xstream.registerConverter(new DefaultUrlConverter(client));
		return (T) xstream.fromXML(content);
	}

	private List<Class> getTypesToEnhance() {
		return typesToEnhance;
	}

	private List<String> getCollectionNames(RestClient client) {
		for (Class type : typesToEnhance) {
			String plural = Noun.pluralOf(type.getSimpleName(), client.inflectionRules());
			names.add(Character.toLowerCase(plural.charAt(0)) + plural.substring(1));
		}
		return names;
	}

	public XmlMediaType withTypes(Class... classes) {
		this.typesToEnhance.addAll(Arrays.asList(classes));
		return this;
	}

	private XStream getXstream(RestClient client) {
		if (xstream == null) {
			this.xstream = helper.getXStream(getTypesToEnhance(), getCollectionNames(client));
			configure(xstream);
		}
		return xstream;
	}

	public void withCollectionName(String... names) {
		for (String name : names) {
			this.names.add(name);
		}
	}

}
