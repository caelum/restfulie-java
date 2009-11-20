package br.com.caelum.restfulie;

import br.com.caelum.restfulie.http.HttpMethod;

/**
 * Represents a specific transition that can be executed with a resource.
 * @author guilherme silveira
 *
 */
public interface Transition extends TransitionToExecute{
	
	String getHref();
	
	String getRel();
	
}
