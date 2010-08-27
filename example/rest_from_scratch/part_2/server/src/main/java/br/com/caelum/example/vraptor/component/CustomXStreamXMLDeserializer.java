package br.com.caelum.example.vraptor.component;

import br.com.caelum.vraptor.deserialization.XStreamXMLDeserializer;
import br.com.caelum.vraptor.http.ParameterNameProvider;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
@ApplicationScoped
@Component
public class CustomXStreamXMLDeserializer extends XStreamXMLDeserializer {

	public CustomXStreamXMLDeserializer(ParameterNameProvider provider) {
		super(provider);
	}

	/**
	 * Extension point to configure your xstream instance.
	 * @return the configured xstream instance
	 */
	@Override
	protected XStream getXStream() {
		return new XStream(new DomDriver());
	}

}
