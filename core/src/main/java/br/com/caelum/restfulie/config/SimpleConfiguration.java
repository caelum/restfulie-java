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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Basic configuration implementation.
 * 
 * @author guilherme silveira
 */
public class SimpleConfiguration implements Configuration {

	private final Class type;
	private final List<String> excludes = new ArrayList<String>();
	private final List<String> includes = new ArrayList<String>();
	private final List<String> implicits = new ArrayList<String>();

	public SimpleConfiguration(Class type) {
		this.type = type;
	}

	public SimpleConfiguration exclude(String... fields) {
		this.excludes.addAll(Arrays.asList(fields));
		return this;
	}

	public SimpleConfiguration include(String... fields) {
		this.includes.addAll(Arrays.asList(fields));
		return this;
	}

	public String[] getExcludes() {
		return excludes.toArray(new String[excludes.size()]);
	}

	public List<String> getIncludes() {
		return includes;
	}

	public Class getType() {
		return type;
	}

	public SimpleConfiguration implicit(String... fields) {
		this.implicits.addAll(Arrays.asList(fields));
		return this;
	}
	
	public List<String> getImplicits() {
		return implicits;
	}
	
}
