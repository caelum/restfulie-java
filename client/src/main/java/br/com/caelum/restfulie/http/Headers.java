package br.com.caelum.restfulie.http;

import java.util.List;

import br.com.caelum.restfulie.Link;

public interface Headers extends Iterable<Header> {

	String getMain(String string);

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
	List<Link> links();

	/**
	 * Returns a link given its rel
	 */
	Link link(String rel);

}
