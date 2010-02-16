package br.com.caelum.vraptor.restbucks;

import java.util.Calendar;
import java.util.List;

import br.com.caelum.vraptor.restbucks.web.OrderingController;
import br.com.caelum.vraptor.restfulie.Restfulie;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaResource;
import br.com.caelum.vraptor.restfulie.relation.Relation;

public class Receipt implements HypermediaResource{
	
	private final Calendar paymentTime= Calendar.getInstance();
	private final Order order;
	
	public Receipt(Order order) {
		this.order = order;
	}

	public List<Relation> getRelations(Restfulie control) {
		control.transition("order").uses(OrderingController.class).get(order);
		return control.getRelations();
	}

}
