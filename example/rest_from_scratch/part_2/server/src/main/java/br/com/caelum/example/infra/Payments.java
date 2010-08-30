package br.com.caelum.example.infra;

import java.util.HashMap;
import java.util.Map;

import br.com.caelum.example.model.Payment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class Payments {

	private Map<Long, Payment> payments = new HashMap<Long, Payment>();

	private long next;

	public Payment get(Long id) {
		return payments.get(id);
	}

	public void save(Payment payment) {
		long id = next++;
		payment.setId(id);
		payments.put(id, payment);
	}

}
