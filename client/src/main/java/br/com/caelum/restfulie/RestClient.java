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

import br.com.caelum.restfulie.mediatype.MediaTypes;
import br.com.caelum.restfulie.request.RequestDispatcher;

/**
 * Allows resources configuration and access.
 *
 * @author guilherme silveira
 */
public interface RestClient extends RequestEntry {

	/**
	 * Returns the http client provider in use.
	 */
	RequestDispatcher getProvider();

	/**
	 * Returns the last accessed URI
	 */
	URI lastURI();

	/**
	 * Returns the media type registry.
	 */
	MediaTypes getMediaTypes();
	
}