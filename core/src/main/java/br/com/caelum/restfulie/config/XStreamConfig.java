/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource - guilherme.silveira@caelum.com.br
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.caelum.restfulie.config;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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

import br.com.caelum.restfulie.DefaultRelation;
import br.com.caelum.restfulie.Resource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.alias.ClassMapper;
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
	private XStream instance;

	public XStreamConfig(SerializationConfig configs) {
		this.configs = configs;
	}
	
	public synchronized XStream create() {
		if (instance != null) {
			return instance;
		}
		instance = getXStream();
		for(Configuration config : configs.getAllTypes()) {
			Class type = config.getType();
			enhanceResource(type);
			instance.processAnnotations(type);
			excludeNonPrimitives(instance, type, config.getIncludes(), config.getImplicits());
		}
		for(Class customType : realTypes.values()) {
			instance.addImplicitCollection(customType, "link","link", DefaultRelation.class);
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
		qnameMap.registerMapping(qname, DefaultRelation.class);
		ReflectionProvider provider = getProvider();
		XStream xstream = new XStream(provider, new StaxDriver(qnameMap)) {
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new LinkSupportWrapper(next);
			}
		};
		xstream.useAttributeFor(DefaultRelation.class, "rel");
		xstream.useAttributeFor(DefaultRelation.class, "href");
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

	public <T> Class enhanceResource(Class<T> originalType) {
		ClassPool pool = ClassPool.getDefault();
		try {
			// TODO extract this enhancement to an interface and test it appart
			CtClass custom =   pool.makeClass("br.com.caelum.restfulie." + originalType.getSimpleName() + "_" + System.currentTimeMillis());
			custom.setSuperclass(pool.get(originalType.getName()));
			custom.addInterface(pool.get(Resource.class.getName()));
			CtField field = CtField.make("public java.util.List link = new java.util.ArrayList();", custom);
			custom.addField(field);
			custom.addMethod(CtNewMethod.make("public java.util.List getRelations() { return link; }", custom));
			custom.addMethod(CtNewMethod.make("public br.com.caelum.restfulie.Relation getRelation(String rel) { for(int i=0;i<link.size();i++) {br.com.caelum.restfulie.Relation t = link.get(i); if(t.getRel().equals(rel)) return t; } return null; }", custom));
			Class customType = custom.toClass();
			this.realTypes.put(originalType, customType);
			return customType;
		} catch (NotFoundException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		} catch (CannotCompileException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		}
	}

	private boolean isPrimitive(Class<?> type) {
		return type.isPrimitive()
		|| type.isEnum()
		|| Number.class.isAssignableFrom(type)
		|| type.equals(String.class)
		|| Date.class.isAssignableFrom(type)
		|| Calendar.class.isAssignableFrom(type)
		|| Boolean.class.equals(type)
		|| Character.class.equals(type);
	}

	public Configuration type(Class<? extends Object> type) {
		return configs.type(type);
	}

}
