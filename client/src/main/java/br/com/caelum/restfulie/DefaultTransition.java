package br.com.caelum.restfulie;

/**
 * Default implementation of a transition.
 * 
 * @author guilherme silveira
 * @author lucas souza
 */
public class DefaultTransition implements Transition {

	public String getRel() {
		return rel;
	}
	public String getHref() {
		return href;
	}
	private String rel;
	private String href;

}
