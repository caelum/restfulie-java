/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package br.com.caelum.restfulie.serializer;

import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thoughtworks.xstream.XStream;

/**
 * A Xml Serializer based on XStream
 * @author Lucas Cavalcanti
 * @author Guilherme Silveira
 * @since 3.0.2
 */
public class XStreamXmlSerializer implements BasicSerializer {

	private final XStream xstream;
	private final Writer writer;
	private Object toSerialize;
	private final TypeNameExtractor extractor;
	private final Map<Class<?>, List<String>> excludes = new HashMap<Class<?>, List<String>>();

	public XStreamXmlSerializer(XStream xstream, Writer writer, TypeNameExtractor extractor) {
		this.xstream = xstream;
		this.writer = writer;
		this.extractor = extractor;
	}

	private boolean isPrimitive(Class<?> type) {
		return (type.isPrimitive() || type.getName().startsWith("java") || type.isEnum()) &&
			!Collection.class.isAssignableFrom(type);
	}

	public BasicSerializer exclude(String... names) {
		for (String name : names) {
			xstream.omitField(getParentTypeFor(name), getNameFor(name));
		}
		return this;
	}

	private String getNameFor(String name) {
		String[] path = name.split("\\.");
		return path[path.length-1];
	}

	private Class<?> getParentTypeFor(String name) {
		Class<?> type = toSerialize.getClass();
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

	public <T> BasicSerializer from(T object) {
		if (object == null) {
			throw new NullPointerException("You can't serialize null objects");
		}
		if (Collection.class.isInstance(object)) {
			throw new IllegalArgumentException("It's not possible to serialize colections yet. " +
					"Create a class that wraps this collections by now.");
		}
		Class<?> type = object.getClass();
		String name = extractor.nameFor(type);
		xstream.alias(name, type);
		excludeNonPrimitiveFields(type, type);
		this.toSerialize = object;
		return this;
	}

	private void excludeNonPrimitiveFields(Class<?> baseType, Class<?> type) {
		if(type.equals(Object.class)) {
			return;
		}
		if(!excludes.containsKey(baseType)) {
			excludes.put(baseType, new ArrayList<String>());
		}
		for (Field field : type.getDeclaredFields()) {
			if (!isPrimitive(field.getType())) {
				excludes.get(baseType).add(field.getName());
			}
		}
		excludeNonPrimitiveFields(baseType, type.getSuperclass());
	}

	public BasicSerializer include(String... fields) {
		for (String field : fields) {
			Class<?> parentType = getParentTypeFor(field);
			String fieldName = getNameFor(field);
			Type genericType = getField(parentType, fieldName).getGenericType();
			Class<?> fieldType = getActualType(genericType);
			if (isCollection(genericType)) {
				xstream.alias(extractor.nameFor(fieldType), fieldType);
			}
			if (!excludes.containsKey(fieldType)) {
				excludeNonPrimitiveFields(fieldType, fieldType);
			}
			excludes.get(parentType).remove(fieldName);
		}
		return this;
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

	public void serialize() {
		for (Entry<Class<?>, List<String>> excludes : this.excludes.entrySet()) {
			for(String exclude : excludes.getValue()) {
				xstream.omitField(excludes.getKey(), exclude);
			}
		}
		xstream.toXML(toSerialize, writer);
	}


}
