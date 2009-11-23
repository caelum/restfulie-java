package br.com.caelum.restfulie;

import br.com.caelum.restfulie.http.HttpMethod;

public interface TransitionToExecute {

	/**
	 * Executes this transition passing some parameters.
	 */
	<T> Response execute(T arg);

	/**
	 * Executes this transition passing some parameters and will automatically.
	 */
	<T, R> R executeAndRetrieve(T arg);

	/**
	 * Executes this transition without any parameters.
	 */
	Response execute();

	TransitionToExecute method(HttpMethod method);

}
