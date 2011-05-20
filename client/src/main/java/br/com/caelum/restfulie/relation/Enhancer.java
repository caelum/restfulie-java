package br.com.caelum.restfulie.relation;

public interface Enhancer {

	public abstract <T> Class enhanceResource(Class<T> originalType);

}