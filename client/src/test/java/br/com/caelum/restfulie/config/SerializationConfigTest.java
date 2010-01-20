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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationConfigTest {
	
	@Test
	public void shouldReturnANewConfigurationIfThereIsNoneAvailable() {
		SerializationConfig configs = new SerializationConfig();
		Configuration config = configs.type(String.class);
		assertThat(config.getIncludes().size(), is(equalTo(0)));
		assertThat(config.getExcludes().length, is(equalTo(0)));
		assertThat(config.getType(), is(equalTo(String.class)));
	}

	@Test
	public void shouldReturnTheSameConfiguration() {
		Configuration config = mock(Configuration.class);

		Map<Class, Configuration> map = new HashMap<Class,Configuration>();
		map.put(String.class, config);
		SerializationConfig configs = new SerializationConfig(map);
		assertThat(configs.type(String.class), is(equalTo(config)));
	}


}
