package br.com.caelum.restfulie.http;

import java.util.List;

import br.com.caelum.restfulie.Link;

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

	/**
	 * Returns all links on of this header
	 */
	public abstract List<Link> links();
	
	/**
	 * Returns a link given its rel
	 */
	public abstract Link link(String rel);

}
