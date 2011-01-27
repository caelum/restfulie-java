package br.com.caelum.restfulie.http;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jvnet.inflector.Noun;
import org.jvnet.inflector.Pluralizer;

import br.com.caelum.restfulie.RestClient;

public class DefaultRestClientTest 
{

	private RestClient client;
	
	@Test
	public void shouldInflectUsingDefaultInflector()
	{
		client = new DefaultRestClient();
		assertEquals( Noun.pluralOf("loaf", client.inflectionRules()), "loaves" );
	}
	
	@Test
	public void shouldInflectUsingCustomInflector()
	{
		Pluralizer inflector = new Pluralizer() 
		{	
			@Override
			public String pluralize(String word, int number) 
			{
				return "custom works!";
			}
			
			@Override
			public String pluralize(String word) 
			{
				return "custom works!";
			}
		};
		
		client = new DefaultRestClient().withInflector(inflector);
		assertEquals( Noun.pluralOf("loaf", client.inflectionRules()), "custom works!");
	}
	
}