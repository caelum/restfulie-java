package br.com.caelum.restfulie;

import br.com.caelum.restfulie.http.HttpMethod;

public interface TransitionToExecute {

	/**
	 * Executes this transition passing some parameters.
	 */
	<T> Response execute(T arg);

	/**
	 * Executes this transition passing some parameters and return its result.
	 */
	<T, R> R executeAndRetrieve(T arg);
	
	/**
	 * Executes this transition and return its result.
	 */
	<R> R executeAndRetrieve();

	/**
	 * Executes this transition without any parameters.
	 */
	Response execute();

	TransitionToExecute method(HttpMethod method);

}
