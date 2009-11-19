package br.com.caelum.restfulie.config;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
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
import br.com.caelum.restfulie.serializer.DefaultTypeNameExtractor;
import br.com.caelum.restfulie.serializer.TypeNameExtractor;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionProviderWrapper;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class XStreamConfig {

	private final SerializationConfig configs;
	private final Map<Class,Class> realTypes= new HashMap<Class,Class>();
	private final TypeNameExtractor extractor= new DefaultTypeNameExtractor();

	public XStreamConfig(SerializationConfig configs) {
		this.configs = configs;
	}
	
	public XStream create() {
		XStream instance = getXStream();
		for(Configuration config : configs.getAllTypes()) {
			Class type = config.getType();
			enhanceResource(type);
			instance.processAnnotations(type);
			excludeNonPrimitives(instance, type);
			//exclude(instance, type, config.getExcludes());
			//include(instance, type, config.getIncludes());
		}
		for(Class customType : realTypes.values()) {
			instance.addImplicitCollection(customType, "link","link", DefaultTransition.class);
		}
		return instance;
	}

	private void excludeNonPrimitives(XStream stream, Class type) {
		if(type.equals(Object.class)) {
			return;
		}
		for(Field f: type.getDeclaredFields()) {
			if(!isPrimitive(f.getType())) {
				stream.omitField(type, f.getName());
			}
		}
		excludeNonPrimitives(stream, type.getSuperclass());
	}

	private void exclude(XStream xstream, Class type, String... names) {
		for (String name : names) {
			xstream.omitField(getParentTypeFor(type, name), getNameFor(name));
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

	private String getNameFor(String name) {
		String[] path = name.split("\\.");
		return path[path.length-1];
	}

	private Class<?> getParentTypeFor(Class baseType, String name) {
		Class<?> type = baseType;
		String[] path = name.split("\\.");
		for (int i = 0; i < path.length - 1; i++) {
			type = getActualType(
					getField(type, path[i]).getGenericType());
		}
		return type;
	}

	private Field getField(Class<?> type, String name) {
		if(type.equals(Object.class)) {
			throw new IllegalArgumentException("Unable to serialize " + name);
		}
		for(Field f : type.getDeclaredFields()) {
			if(f.getName().equals(name)) {
				return f;
			}
		}
		return getField(type.getSuperclass(), name);
	}

	private Class<?> getActualType(Type genericType) {
		if (genericType instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType) genericType;
			if (isCollection(type)) {
				return (Class<?>) type.getActualTypeArguments()[0];
			}
		}
		return (Class<?>) genericType;
	}


	private boolean isCollection(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType ptype = (ParameterizedType) type;
			return Collection.class.isAssignableFrom((Class<?>) ptype.getRawType());
		}
		return Collection.class.isAssignableFrom((Class<?>) type);
	}

	public void include(XStream xstream, Class type, String[] includes) {
		if(true) return;
		for (String field : includes) {
			Class<?> parentType = getParentTypeFor(type, field);
			String fieldName = getNameFor(field);
			Type genericType = getField(parentType, fieldName).getGenericType();
			Class<?> fieldType = getActualType(genericType);
			if (isCollection(genericType)) {
				xstream.alias(extractor.nameFor(fieldType), fieldType);
			}
			if (!configs.contains(fieldType)) {
				excludeNonPrimitiveFields(fieldType, fieldType);
			}
			if(configs.contains(parentType)) {
				// might give an iteration error? (failsafe iteration)
				configs.type(parentType).exclude(fieldName);
			}
		}
	}

	private boolean isPrimitive(Class<?> type) {
		return (type.isPrimitive() || type.getName().startsWith("java") || type.isEnum()) &&
			!Collection.class.isAssignableFrom(type);
	}

	private void excludeNonPrimitiveFields(Class<?> baseType, Class<?> type) {
		if(type.equals(Object.class)) {
			return;
		}
		for (Field field : type.getDeclaredFields()) {
			if (!isPrimitive(field.getType())) {
				// might give an iteration error? (failsafe iteration)
				configs.type(baseType).exclude(field.getName());
			}
		}
		excludeNonPrimitiveFields(baseType, type.getSuperclass());
	}
}
