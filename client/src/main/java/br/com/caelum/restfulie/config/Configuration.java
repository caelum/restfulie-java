package br.com.caelum.restfulie.config;

import java.util.List;

/**
 * Configuration representation for a resource.
 * 
 * @author guilherme silveira
 */
public interface Configuration {

	Configuration include(String... fields);

	Configuration exclude(String... fields);
	
	/**
	 * Add implicit collections for those fields
	 */
	Configuration implicit(String... fields);

	List<String> getIncludes();
	List<String> getImplicits();

	String[] getExcludes();

	Class getType();
}
