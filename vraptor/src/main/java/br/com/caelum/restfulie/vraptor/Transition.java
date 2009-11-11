package br.com.caelum.restfulie.vraptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Notifies restfulie that this method should be trated as a transition,
 * therefore runs the interceptor before this method.
 * 
 * @author guilherme silveira
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transition {

}
