package br.com.caelum.example.vraptor.component;

import br.com.caelum.example.model.Item;
import br.com.caelum.vraptor.deserialization.JsonDeserializer;
import br.com.caelum.vraptor.http.ParameterNameProvider;
import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

import com.thoughtworks.xstream.XStream;

@ApplicationScoped
@Component
public class CustomJsonDeserializer extends JsonDeserializer {

	public CustomJsonDeserializer(ParameterNameProvider provider,
			TypeNameExtractor extractor) {
		super(provider, extractor);
	}

	/**
	 * Extension point to configure your xstream instance.
	 * @return the configured xstream instance
	 */
	protected XStream getXStream() {
		XStream xstream = super.getXStream();
		xstream.processAnnotations(Item.class);
		return xstream;
	}

}
