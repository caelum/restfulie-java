package br.com.caelum.restfulie.http;

import java.util.List;

public interface Headers {

	public abstract String getMain(String string);

	/**
	 * Returns a list with all values for this header
	 */
	List<String> get(String key);

	/**
	 * Returns the first appearance of this header
	 */
	String getFirst(String key);

}
