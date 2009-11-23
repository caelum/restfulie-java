package br.com.caelum.restfulie;


/**
 * Represents a specific transition that can be executed with a resource.
 * @author guilherme silveira
 *
 */
public interface Transition extends TransitionToExecute{
	
	String getHref();
	
	String getRel();
	
}
