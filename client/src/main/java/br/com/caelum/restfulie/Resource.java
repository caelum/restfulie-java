package br.com.caelum.restfulie;

import java.util.Collection;

/**
 * A single resource.<br>
 * Deserialized objects will implement this interface and support transition
 * methods.
 * 
 * @author guilherme silveira
 * @author lucas souza
 */
public interface Resource {

	/**
	 * Returns a list of possible transitions given this resource's state.
	 * 
	 * @return the collection of transitions
	 */
	Collection<Transition> getTransitions();

}
