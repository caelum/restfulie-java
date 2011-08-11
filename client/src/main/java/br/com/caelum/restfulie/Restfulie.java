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

import br.com.caelum.restfulie.http.DefaultHttpRequest;
import br.com.caelum.restfulie.http.DefaultRestClient;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.mediatype.GsonMediaType;

/**
 * Restfulie's client API entry point.<br/>

 * @author guilherme silveira
 */
public class Restfulie {
	
	/**
	 * Given an retrieved resource, gives access to restfulie's transition api.
	 */
	public static <T> Resource resource(T object) {
		return (Resource) object;
	}

	/**
	 * Entry point to configure serialization data prior to accessing the resources.
	 */
	public static RestClient custom() {
		return new DefaultRestClient();
	}

	/**
	 * Entry point to configure serialization data prior to accessing the resources (json only).
	 */
	public static RestClient jsonOnly() {
		return new DefaultRestClient( new GsonMediaType() );
	}
	
	/**
	 * Entry point to direct access an uri.
	 */
	public static Request at(URI uri) {
		RestClient client = custom();
		return new DefaultHttpRequest(uri, client).accept("application/xml");
	}

	/**
	 * Entry point to direct access an uri.
	 * @throws URISyntaxException 
	 */
	public static Request at(String uri) {
		try {
			return at(new URI(uri));
		} catch (URISyntaxException e) {
			throw new RestfulieException("Invalid URI Syntax for " + uri, e);
		}
	}

}
