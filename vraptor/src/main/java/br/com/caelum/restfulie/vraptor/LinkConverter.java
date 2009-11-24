package br.com.caelum.restfulie.vraptor;

import br.com.caelum.vraptor.config.Configuration;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.rest.Restfulie;
import br.com.caelum.vraptor.rest.StateResource;
import br.com.caelum.vraptor.rest.Transition;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Reads all transitions from your resource object and converts them into link
 * elements.<br>
 * The converter passed in the constructor will be used to marshall the rest of
 * the object.
 * 
 * @author guilherme silveira
 * @author lucas cavalcanti
 */
public class LinkConverter implements Converter {

	private final Restfulie restfulie;
	private final Converter base;
	private final Configuration config;

	public LinkConverter(Converter base, Restfulie restfulie, Configuration config) {
		this.base = base;
		this.restfulie = restfulie;
		this.config = config;
	}

	public void marshal(Object root, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		base.marshal(root, writer, context);
		StateResource resource = (StateResource) root;
		for (Transition t : resource.getFollowingTransitions(restfulie)) {
			writer.startNode("atom:link");
			writer.addAttribute("rel", t.getName());
			writer.addAttribute("href", config.getApplicationPath() + t.getUri());
			writer.addAttribute("xmlns:atom", "http://www.w3.org/2005/Atom");
			writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader arg0,
			UnmarshallingContext arg1) {
		return base.unmarshal(arg0, arg1);
	}

	public boolean canConvert(Class type) {
		return StateResource.class.isAssignableFrom(type)
				&& base.canConvert(type);
	}

}
