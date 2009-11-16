package br.com.caelum.restfulie;

/**
 * Basic resource serialization support.
 * 
 * @author guilherme silveira
 */
public interface BasicResourceSerializer {

	BasicResourceSerializer exclude(String... names);

	BasicResourceSerializer include(String... names);

	<R> R post();
	
	<R> R get();

}
