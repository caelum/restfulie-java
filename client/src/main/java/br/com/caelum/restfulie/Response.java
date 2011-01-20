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

import java.io.IOException;
import java.net.URI;
import java.util.List;

import br.com.caelum.restfulie.http.Headers;
import br.com.caelum.restfulie.http.Request;

/**
 * A transition execution response.<br>
 * Through this interface, one can either access the original response
 * (depending on the http layer provider chosen) or, if any, the resulting
 * resource.
 * 
 * @author guilherme silveira
 */
public interface Response {

	public int getCode();

	public String getContent();

	public List<String> getHeader(String key);

	/**
	 * Returns the resource if any resource can be parsed from this response's content.
	 * @throws IOException 
	 */
	public <T> T getResource() throws IOException;

	public Headers getHeaders();

	URI getLocation();

	public String getType();

	public Request getRequest();
	
}
