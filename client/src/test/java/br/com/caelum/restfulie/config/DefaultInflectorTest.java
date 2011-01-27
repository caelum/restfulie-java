package br.com.caelum.restfulie.config;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jvnet.inflector.Noun;

import br.com.caelum.restfulie.DefaultInflector;


public class DefaultInflectorTest 
{

	@Test
	public void shouldInflectPessoa()
	{
		assertEquals(Noun.pluralOf("loaf", new DefaultInflector()), "loaves");
	}
	
}