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

import br.com.caelum.restfulie.config.Configuration;
import br.com.caelum.restfulie.config.SimpleConfiguration;

import com.thoughtworks.xstream.XStream;

/**
 * A Xml Serializer based on XStream
 * 
 * @author Lucas Cavalcanti
 * @author Guilherme Silveira
 * @since 3.0.2
 */
public class XStreamXmlSerializer implements BasicSerializer {

	private final XStream xstream;
	private final Writer writer;
	private Object toSerialize;

	public XStreamXmlSerializer(XStream xstream, Writer writer) {
		this.xstream = xstream;
		this.writer = writer;
	}

	public <T> BasicSerializer from(T object) {
		return from(object, null);
	}

	public void serialize() {
		xstream.toXML(toSerialize, writer);
	}

	public BasicSerializer from(Object object, Configuration config) {
		if (object == null) {
			throw new NullPointerException("You can't serialize null objects");
		}
		if (config == null) {
			config = new SimpleConfiguration(object.getClass());
		}
		this.toSerialize = object;
		return this;
	}

}
