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

package br.com.caelum.restfulie.client;

import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.http.DefaultRelation;
import br.com.caelum.restfulie.mediatype.MediaTypes;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Default transition converter.<br>
 * Injects the current deserializer to all transitions.
 * 
 * @author guilherme silveira
 * 
 */
public class DefaultLinkConverter implements Converter {

	private final RestClient client;

	public DefaultLinkConverter(RestClient client) {
		this.client = client;
	}

	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		// marshalling does not do anything
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		String rel = reader.getAttribute("rel");
		String href = reader.getAttribute("href");
		return new DefaultRelation(rel, href, client);
	}

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class type) {
		return type.equals(DefaultRelation.class);
	}

}
