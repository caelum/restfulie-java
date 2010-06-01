package br.com.caelum.example.vraptor.component;

import java.io.InputStream;
import java.lang.reflect.Method;

import br.com.caelum.vraptor.deserialization.XMLDeserializer;
import br.com.caelum.vraptor.http.ParameterNameProvider;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
//TODO - Eu copiei essa classe porcamente do VRaptor e adicionei o DomDriver... melhorar isso
@ApplicationScoped
@Component
public class CustomXStreamXMLDeserializer implements XMLDeserializer {

	private final ParameterNameProvider provider;

	public CustomXStreamXMLDeserializer(ParameterNameProvider provider) {
		this.provider = provider;
	}

	public Object[] deserialize(InputStream inputStream, ResourceMethod method) {
		Method javaMethod = method.getMethod();
		Class<?>[] types = javaMethod.getParameterTypes();
		if (types.length == 0) {
			throw new IllegalArgumentException("Methods that consumes xml must receive just one argument: the xml root element");
		}
		XStream xStream = getConfiguredXStream(javaMethod, types);

		Object[] params = new Object[types.length];

		chooseParam(types, params, xStream.fromXML(inputStream));

		return params;
	}

	/**
	 * Returns an xstream instance already configured.
	 */
	public XStream getConfiguredXStream(Method javaMethod, Class<?>[] types) {
		XStream xStream = getXStream();
		aliasParams(javaMethod, types, xStream);
		return xStream;
	}

	private void chooseParam(Class<?>[] types, Object[] params, Object deserialized) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].isInstance(deserialized)) {
				params[i] = deserialized;
			}
		}
	}

	private void aliasParams(Method method, Class<?>[] types, XStream deserializer) {
		String[] names = provider.parameterNamesFor(method);
		for (int i = 0; i < names.length; i++) {
			deserializer.alias(names[i], types[i]);
		}
	}

	/**
	 * Extension point to configure your xstream instance.
	 * @return the configured xstream instance
	 */
	protected XStream getXStream() {
		return new XStream(new DomDriver());
	}

}
