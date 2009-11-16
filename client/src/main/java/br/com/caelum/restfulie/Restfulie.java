package br.com.caelum.restfulie;

public class Restfulie {

	public static <T> Resource resource(T object) {
		return (Resource) object;
	}

}
