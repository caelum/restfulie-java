package br.com.caelum.restfulie.marshall;

/**
 * Basic resource serialization support.
 * 
 * @author guilherme silveira
 */
public interface ResourceSerializer {

	ResourceSerializer exclude(String... names);

	ResourceSerializer include(String... names);

	<R> R post();
	
	<R> R get();

	public <T, R> R post(T object);

}
