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

import java.util.List;

/**
 * A single resource.<br>
 * Deserialized objects will implement this interface and support transition
 * methods.
 * 
 * @author guilherme silveira
 * @author lucas souza
 */
public interface Resource {

	/**
	 * Returns a list of possible transitions given this resource's state.
	 * 
	 * @return the collection of transitions
	 */
	List<Transition> getTransitions();
	
	Transition getTransition(String rel);

}
