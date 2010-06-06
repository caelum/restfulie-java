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

import br.com.caelum.restfulie.http.ApacheHttpClientProvider;
import br.com.caelum.restfulie.http.HttpClientProvider;
import br.com.caelum.restfulie.http.JsonMediaType;
import br.com.caelum.restfulie.http.MediaTypes;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.http.XmlMediaType;

/**
 * Configured service entry point.
 * 
 * @author guilherme silveira
 */
public class DefaultRestClient implements RestClient {

	private final MediaTypes types = new MediaTypes();
	
	private final HttpClientProvider provider;
	
	public DefaultRestClient() {
		provider = new ApacheHttpClientProvider();
		types.register(new XmlMediaType());
		types.register(new JsonMediaType());
	}
	
	@Override
	public HttpClientProvider getProvider() {
		return provider;
	}

	@Override
	public MediaTypes getMediaTypes() {
		return types;
	}

	/**
	 * Entry point to direct access an uri.
	 */
	public Request at(URI uri) {
		return getProvider().request(uri, this).accept("application/xml");
	}

	/**
	 * Entry point to direct access an uri.
	 * @throws URISyntaxException 
	 */
	public Request at(String uri) throws URISyntaxException {
		return at(new URI(uri));
	}
}
