package br.com.caelum.restfulie.vraptor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeSearcher {

	private ParameterizedType executeFor(Class control, Class<?> baseType) {
		if(baseType.equals(Object.class)) {
			throw new IllegalStateException(
					"Unable to detect which state control it is because "
							+ control.getClass()
							+ " does not implement StateControl at all.");
		}
		Type[] interfaces = baseType.getGenericInterfaces();
		for (Type type : interfaces) {
			if (!(type instanceof ParameterizedType)) {
				throw new IllegalStateException(
						"Unable to detect which state control it is because "
								+ control.getClass()
								+ " does not implement StateControl of an specific type");
			}
			ParameterizedType parameterized = (ParameterizedType) type;
			if(parameterized.getRawType().equals(StateControl.class)) {
				return parameterized;
			}
		}
		return executeFor(control, baseType.getSuperclass());
	}

	public ParameterizedType search(Class at) {
		return executeFor(at, at);
	}

}
