package br.com.caelum.restfulie.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Basic configuration implementation.
 * 
 * @author guilherme silveira
 */
public class SimpleConfiguration implements Configuration {

	private final Class type;
	private final List<String> excludes = new ArrayList<String>();
	private final List<String> includes = new ArrayList<String>();
	private final List<String> implicits = new ArrayList<String>();

	public SimpleConfiguration(Class type) {
		this.type = type;
	}

	public SimpleConfiguration exclude(String... fields) {
		this.excludes.addAll(Arrays.asList(fields));
		return this;
	}

	public SimpleConfiguration include(String... fields) {
		this.includes.addAll(Arrays.asList(fields));
		return this;
	}

	public String[] getExcludes() {
		return excludes.toArray(new String[excludes.size()]);
	}

	public List<String> getIncludes() {
		return includes;
	}

	public Class getType() {
		return type;
	}

	public SimpleConfiguration implicit(String... fields) {
		this.implicits.addAll(Arrays.asList(fields));
		return this;
	}
	
	public List<String> getImplicits() {
		return implicits;
	}
	
}
