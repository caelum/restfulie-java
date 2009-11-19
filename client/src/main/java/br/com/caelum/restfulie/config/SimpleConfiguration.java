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

	public SimpleConfiguration(Class type) {
		this.type = type;
	}

	public void exclude(String... fields) {
		this.excludes.addAll(Arrays.asList(fields));
	}

	public void include(String... fields) {
		this.includes.addAll(Arrays.asList(fields));
	}

	public String[] getExcludes() {
		return excludes.toArray(new String[excludes.size()]);
	}

	public String[] getIncludes() {
		return includes.toArray(new String[includes.size()]);
	}

	public Class getType() {
		return type;
	}

}
