package br.com.caelum.restfulie;

/**
 * Represents a specific transition that can be executed with a resource.
 * @author guilherme silveira
 *
 */
public interface Transition {
	
	String getHref();
	
	String getRel();

	/**
	 * Executes this transition passing some parameters.
	 */
	<T> Response execute(T arg);

	/**
	 * Executes this transition without any parameters.
	 */
	<T> Response execute();

}
