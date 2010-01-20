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

package br.com.caelum.restfulie;

import br.com.caelum.restfulie.client.DefaultTransitionConverter;
import br.com.caelum.restfulie.config.SerializationConfig;
import br.com.caelum.restfulie.config.XStreamConfig;
import br.com.caelum.restfulie.unmarshall.Deserializer;

import com.thoughtworks.xstream.XStream;

/**
 * Deserialization support through xstream.
 * 
 * @author guilherme silveira
 * @author lucas souza
 * 
 */
public class XStreamDeserializer implements Deserializer {

	private final XStream xstream;
	
	public XStreamDeserializer(XStreamConfig config) {
		this.xstream = config.create();
		this.xstream.registerConverter(new DefaultTransitionConverter(config, this));
	}

	public XStreamDeserializer(XStream xStream) {
		this.xstream = xStream;
		this.xstream.registerConverter(new DefaultTransitionConverter(new XStreamConfig(new SerializationConfig()), this));
	}

	public Object fromXml(String xml) {
		return xstream.fromXML(xml);
	}
	
}
