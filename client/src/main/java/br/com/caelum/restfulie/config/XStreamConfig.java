package br.com.caelum.restfulie.config;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import javax.xml.namespace.QName;

import br.com.caelum.restfulie.DefaultTransition;
import br.com.caelum.restfulie.Resource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionProviderWrapper;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

@SuppressWarnings("unchecked")
public class XStreamConfig {

	private final SerializationConfig configs;
	private final Map<Class,Class> realTypes= new HashMap<Class,Class>();

	public XStreamConfig(SerializationConfig configs) {
		this.configs = configs;
	}
	
	public XStream create() {
		XStream instance = getXStream();
		for(Configuration config : configs.getAllTypes()) {
			Class type = config.getType();
			enhanceResource(type);
			instance.processAnnotations(type);
			excludeNonPrimitives(instance, type, config.getIncludes(), config.getImplicits());
		}
		for(Class customType : realTypes.values()) {
			instance.addImplicitCollection(customType, "link","link", DefaultTransition.class);
		}
		return instance;
	}

	private void excludeNonPrimitives(XStream stream, Class type, List<String> forcedIncludes, List<String> implicits) {
		if(type.equals(Object.class)) {
			return;
		}
		for(Field f: type.getDeclaredFields()) {
			boolean forceInclude = forcedIncludes.contains(f.getName()) || implicits.contains(f.getName());
			if(implicits.contains(f.getName())) {
				stream.addImplicitCollection(type, f.getName());
			}
			if(forceInclude || isPrimitive(f.getType())) {
				continue;
			}
			stream.omitField(type, f.getName());
		}
		excludeNonPrimitives(stream, type.getSuperclass(), forcedIncludes, implicits);
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

	public <T> void enhanceResource(Class<T> originalType) {
		ClassPool pool = ClassPool.getDefault();
		try {
			// TODO extract this enhancement to an interface and test it appart
			CtClass custom =   pool.makeClass("br.com.caelum.restfulie." + originalType.getSimpleName() + "_" + System.currentTimeMillis());
			custom.setSuperclass(pool.get(originalType.getName()));
			custom.addInterface(pool.get(Resource.class.getName()));
			CtField field = CtField.make("public java.util.List link = new java.util.ArrayList();", custom);
			custom.addField(field);
			custom.addMethod(CtNewMethod.make("public java.util.List getTransitions() { return link; }", custom));
			custom.addMethod(CtNewMethod.make("public br.com.caelum.restfulie.Transition getTransition(String rel) { for(int i=0;i<link.size();i++) {br.com.caelum.restfulie.Transition t = link.get(i); if(t.getRel().equals(rel)) return t; } return null; }", custom));
			Class customType = custom.toClass();
			// xstream.addImplicitCollection(customType, "link","link", DefaultTransition.class);
			this.realTypes.put(originalType, customType);
		} catch (NotFoundException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		} catch (CannotCompileException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		}
	}

	private boolean isPrimitive(Class<?> type) {
		return (type.isPrimitive() || type.getName().startsWith("java") || type.isEnum()) &&
			!Collection.class.isAssignableFrom(type);
	}

}
