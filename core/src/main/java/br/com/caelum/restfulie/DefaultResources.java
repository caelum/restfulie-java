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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import br.com.caelum.restfulie.config.Configuration;
import br.com.caelum.restfulie.config.SerializationConfig;
import br.com.caelum.restfulie.config.SimpleConfiguration;
import br.com.caelum.restfulie.config.XStreamConfig;
import br.com.caelum.restfulie.marshall.ResourceSerializer;
import br.com.caelum.restfulie.unmarshall.Deserializer;

/**
 * Configured service entry point.
 * 
 * @author guilherme silveira
 */
public class DefaultResources implements Resources {

	private final Map<Class, Configuration> configurations = new HashMap<Class, Configuration>();

	public Configuration configure(Class type) {
		Configuration config = new SimpleConfiguration(type);
		this.configurations.put(type, config);
		return config;
	}

	public ResourceSerializer entryAt(URI uri) {
		return new EntryPointService(uri, configurations);
	}

	public Deserializer getDeserializer() {
		return new XStreamDeserializer(new XStreamConfig(new SerializationConfig(configurations)));
	}

	public ResourceSerializer entryAt(String uri) throws URISyntaxException {
		return entryAt(new URI(uri));
	}

}
