package br.com.caelum.restfulie.config;

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

	Class getType();
}
