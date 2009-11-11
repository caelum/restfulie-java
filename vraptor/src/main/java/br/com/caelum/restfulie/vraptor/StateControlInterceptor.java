package br.com.caelum.restfulie.vraptor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.core.Routes;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.HttpMethod;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.rest.Restfulie;
import br.com.caelum.vraptor.rest.StateResource;
import br.com.caelum.vraptor.view.Status;

/**
 * Intercepts invocations to state control's intercepted controllers.
 * 
 * @author guilherme silveira
 * @author pedro mariano
 */
public class StateControlInterceptor<T extends StateResource> implements Interceptor {

	private final StateControl<T> control;
	@SuppressWarnings("unchecked")
	private final List<Class> controllers;
	private final Status status;
	private final Restfulie restfulie;
	private final Routes routes;
	private final RequestInfo info;

	public StateControlInterceptor(StateControl<T> control, Restfulie restfulie, Status status, RequestInfo info, Routes routes) {
		this.control = control;
		this.restfulie = restfulie;
		this.status = status;
		this.info = info;
		this.routes = routes;
		this.controllers = Arrays.asList(control.getControllers());
	}

	public boolean accepts(ResourceMethod method) {
		return controllers.contains(method.getResource().getType()) && method.getMethod().isAnnotationPresent(Transition.class);
	}

	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object instance) throws InterceptionException {
		if(executeFor(control.getClass(), method)) {
			stack.next(method, instance);
		}
	}

	private boolean executeFor(Class<?> baseType, ResourceMethod method) {
		if(baseType.equals(Object.class)) {
			throw new IllegalStateException(
					"Unable to detect which state control it is because "
							+ control.getClass()
							+ " does not implement StateControl at all.");
		}
		Type[] interfaces = baseType.getGenericInterfaces();
		for (Type type : interfaces) {
			if (type instanceof ParameterizedType) {
				ParameterizedType parameterized = (ParameterizedType) type;
				if(parameterized.getRawType().equals(StateControl.class)) {
					Type parameterType = parameterized.getActualTypeArguments()[0];
					Class found = (Class) parameterType;
					String parameterName = lowerFirstChar(found.getSimpleName()) + ".id";
					String id = info.getRequest().getParameter(parameterName);
					T resource = control.retrieve(id);
					if(resource==null) {
						status.notFound();
						return false;
					}
					List<br.com.caelum.vraptor.rest.Transition> transitions = resource.getFollowingTransitions(restfulie);
					for (br.com.caelum.vraptor.rest.Transition transition : transitions) {
						if(transition.matches(method.getMethod())) {
							return true;
						}
					}
					EnumSet<HttpMethod> allowed = routes.allowedMethodsFor(info.getRequestedUri());
					allowed.remove(HttpMethod.of(info.getRequest()));
					status.methodNotAllowed(allowed);
					return false;
				}
			} else {
				Class simple = (Class) type;
				if (simple.equals(StateControl.class)) {
					throw new IllegalStateException(
							"Unable to detect which state control it is because "
									+ control.getClass()
									+ " does not implement StateControl of an specific type");
				}
			}
		}
		return executeFor(baseType.getSuperclass(), method);
	}

	private String lowerFirstChar(String simpleName) {
		if(simpleName.length()==1) {
			return simpleName.toLowerCase();
		}
		return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
	}

}
