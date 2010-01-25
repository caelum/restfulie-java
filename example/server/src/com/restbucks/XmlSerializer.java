package com.restbucks;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.restfulie.vraptor.RestfulSerialization;
import br.com.caelum.vraptor.config.Configuration;
import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.rest.Restfulie;

import com.thoughtworks.xstream.XStream;

@Component
@RequestScoped
public class XmlSerializer extends RestfulSerialization{

	public XmlSerializer(HttpServletResponse response,
			TypeNameExtractor extractor, Restfulie restfulie, Configuration config) {
		super(response, extractor, restfulie, config);
	}
	
	@Override
	protected XStream getXStream() {
		XStream instance = super.getXStream();
		instance.processAnnotations(Order.class);
		instance.processAnnotations(Item.class);
		return instance;
	}

}
