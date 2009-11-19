package br.com.caelum.restfulie;


/**
 * Anything went wrong with executing a transition.
 * @author guilherme silveira
 *
 */
public class TransitionException extends RuntimeException {

	public TransitionException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3766001815829700069L;

}
