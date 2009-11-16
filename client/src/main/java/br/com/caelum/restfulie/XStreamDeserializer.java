package br.com.caelum.restfulie;

import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import javax.xml.namespace.QName;

import br.com.caelum.restfulie.client.DefaultTransitionConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionProviderWrapper;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * Deserialization support through xstream.
 * 
 * @author guilherme silveira
 * @author lucas souza
 * 
 */
public class XStreamDeserializer implements Deserializer {

	@SuppressWarnings("unchecked")
	private final Map<Class,Class> realTypes= new HashMap<Class,Class>();
	private final XStream xstream;
	
	public XStreamDeserializer() {
		this.xstream = getXStream();
		// gs: nasty
		this.xstream.registerConverter(new DefaultTransitionConverter(this));
	}

	public XStreamDeserializer(XStream xStream) {
		this.xstream = xStream;
		// gs: nasty
		this.xstream.registerConverter(new DefaultTransitionConverter(this));
	}

	public Object fromXml(String xml) {
		return xstream.fromXML(xml);
	}
	
	private class LinkSupportWrapper extends MapperWrapper{

		public LinkSupportWrapper(Mapper wrapped) {
			super(wrapped);
		}
		
		@Override
		public String getFieldNameForItemTypeAndName(Class definedIn,
				Class itemType, String itemFieldName) {
			if(realTypes.containsKey(definedIn) && itemFieldName.equals("link")) {
				return "link";
			}
			return super.getFieldNameForItemTypeAndName(definedIn, itemType, itemFieldName);
		}
	
	}

	/**
	 * Extension point to configure your xstream instance.
	 * 
	 * @return the xstream instance to use for deserialization
	 */
	protected XStream getXStream() {
		QNameMap qnameMap = new QNameMap();
		QName qname = new QName("http://www.w3.org/2005/Atom", "atom");
		qnameMap.registerMapping(qname, DefaultTransition.class);
		ReflectionProvider provider = getProvider();
		XStream xstream = new XStream(provider, new StaxDriver(qnameMap)) {
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new LinkSupportWrapper(next);
			}
		};
		xstream.useAttributeFor(DefaultTransition.class, "rel");
		xstream.useAttributeFor(DefaultTransition.class, "href");
		return xstream;
	}

	/**
	 * Extension point to define your own provider.
	 * @return
	 */
	private ReflectionProvider getProvider() {
		return new ReflectionProviderWrapper(new Sun14ReflectionProvider()) {
			@Override
			public Object newInstance(Class originalType) {
				if(realTypes.containsKey(originalType)) {
					return super.newInstance(realTypes.get(originalType));
				}
				return super.newInstance(originalType);
			}
			
		};
	}

	public <T> void enhanceResource(Class<T> originalType) {
		ClassPool pool = ClassPool.getDefault();
		try {
			CtClass custom =   pool.makeClass("br.com.caelum.restfulie." + originalType.getSimpleName() + "_" + System.currentTimeMillis());
			custom.setSuperclass(pool.get(originalType.getName()));
			custom.addInterface(pool.get(Resource.class.getName()));
			CtField field = CtField.make("public java.util.List link = new java.util.ArrayList();", custom);
			custom.addField(field);
			custom.addMethod(CtNewMethod.make("public java.util.List getTransitions() { return link; }", custom));
			custom.addMethod(CtNewMethod.make("public br.com.caelum.restfulie.Transition getTransition(String rel) { for(int i=0;i<link.size();i++) {br.com.caelum.restfulie.Transition t = link.get(i); if(t.getRel().equals(rel)) return t; } return null; }", custom));
			Class customType = custom.toClass();
			xstream.addImplicitCollection(customType, "link","link", DefaultTransition.class);
			this.realTypes.put(originalType, customType);
		} catch (NotFoundException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		} catch (CannotCompileException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		}
	}

}
