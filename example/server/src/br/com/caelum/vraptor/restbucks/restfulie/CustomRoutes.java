package br.com.caelum.vraptor.restbucks.restfulie;

import java.lang.reflect.TypeVariable;

import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.http.route.RoutesConfiguration;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.restbucks.OrderMachineController;


@ApplicationScoped
public class CustomRoutes implements RoutesConfiguration{

	public void config(Router router) {
		Class<?> type = OrderMachineController.class;
		TypeVariable<?>[] types = type.getTypeParameters();
		Class<? extends TypeVariable> entityType = types[0].getClass();
		try {
			Class<?> stateType = Class.forName(entityType.getName() + "StateControl");
			//StateControl control = (StateControl) stateType.newInstance();
			//List<Transition> transitions = control.getTransitions();
			//for (Transition transition : transitions) {
			//	
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
