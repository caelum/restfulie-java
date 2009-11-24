package br.com.caelum.restfulie.vraptor;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.config.Configuration;
import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.rest.Restfulie;
import br.com.caelum.vraptor.serialization.XStreamXMLSerialization;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;

@Component
@RequestScoped
public class RestfulSerialization extends XStreamXMLSerialization {
	
	private final Restfulie restfulie;
	private final Configuration config;

	public RestfulSerialization(HttpServletResponse response, TypeNameExtractor extractor, Restfulie restfulie, Configuration config) {
		super(response,extractor);
		this.restfulie = restfulie;
		this.config = config;
	}

	/**
	 * You can override this method for configuring XStream before serialization.
	 * It configures the xstream instance with a link converter for all StateResource implementations.
	 */
	@Override
	protected XStream getXStream() {
		XStream xStream = new XStream();
		xStream.registerConverter(new LinkConverter(new ReflectionConverter(xStream.getMapper(), xStream.getReflectionProvider()), restfulie, config));
		return xStream;
	}

}
