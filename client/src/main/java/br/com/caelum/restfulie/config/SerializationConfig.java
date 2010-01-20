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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles a set of configurations.
 * 
 * @author guilherme silveira
 */
public class SerializationConfig {

	private final Map<Class, Configuration> configs;
	
	public SerializationConfig(Map<Class, Configuration> configs) {
		this.configs = configs;
	}

	public SerializationConfig() {
		this(new HashMap<Class,Configuration>());
	}

	public Configuration type(Class<? extends Object> type) {
		if (!configs.containsKey(type)) {
			configs.put(type, new SimpleConfiguration(type));
		}
		return configs.get(type);
	}

	public Collection<Configuration> getAllTypes() {
		return configs.values();
	}

	public boolean contains(Class<?> type) {
		return configs.containsKey(type);
	}

}
