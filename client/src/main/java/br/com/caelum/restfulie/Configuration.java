package br.com.caelum.restfulie;

/**
 * Configuration representation for a resource.
 * 
 * @author guilherme silveira
 */
public interface Configuration {

	void include(String... fields);

	void exclude(String... fields);

	String[] getIncludes();

	String[] getExcludes();
}
