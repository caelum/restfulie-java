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
		assertThat(config.getType().toString(), is(equalTo(String.class.toString())));
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
