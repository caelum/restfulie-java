package br.com.caelum.vraptor.restbucks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.caelum.vraptor.restbucks.web.ItemController;
import br.com.caelum.vraptor.restbucks.web.OrderingController;
import br.com.caelum.vraptor.restbucks.web.PaymentController;
import br.com.caelum.vraptor.restfulie.Restfulie;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaResource;
import br.com.caelum.vraptor.restfulie.relation.Relation;
import br.com.caelum.vraptor.restfulie.resource.Cacheable;
import br.com.caelum.vraptor.restfulie.serialization.XStreamSerialize;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("order")
public class Order implements HypermediaResource, Cacheable {

	private String id;
	private Location location;
	private List<Item> items = new ArrayList<Item>();

	private transient String status;
	private Payment payment;
	private Receipt receipt;
	
	/**
	 * Cacheable for two hours.
	 */
	public int getMaximumAge() {
		return 2 * 60 * 60; 
	}

	public enum Location {
		TO_TAKE, EAT_IN
	};

	public Order(String status, List<Item> items, Location location) {
		this.status = status;
		this.items = items;
		this.location = location;
	}

	public Order() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XStreamSerialize
	public String getStatus() {
		if(isReady()) {
			return "ready";
		}
		return status;
	}

	boolean isReady() {
		return status.equals("preparing") && payment.getCreatedAt().before(tenSecondsAgo());
	}

	private Calendar tenSecondsAgo() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.SECOND, -10);
		return now;
	}

	public void cancel() {
		status = "cancelled";
	}

	public List<Relation> getRelations(Restfulie control) {
		control.relation("self").uses(OrderingController.class).get(this);
		if (status.equals("unpaid")) {
			control.transition("update").uses(OrderingController.class).update(this);
			control.transition("cancel").uses(OrderingController.class).cancel(this);
			control.relation("payment").uses(OrderingController.class).pay(this,null);
		}
		if(getStatus().equals("ready")) {
			control.transition("retrieve").uses(OrderingController.class).cancel(this);
		}
		if(getStatus().equals("delivered")){
			control.transition("receipt").uses(PaymentController.class).get(this);
		}
		control.relation("items").uses(ItemController.class).index(this);
		return control.getRelations();
	}

	public boolean pay(Payment payment) {
		if(!payment.getAmount().equals(getCost())) {
			return false;
		}
		status = "preparing";
		this.receipt = new Receipt(this);
		this.payment = payment;
		this.payment.setCreatedAt(new GregorianCalendar());
		return true;
	}

	@XStreamSerialize
	public BigDecimal getCost() {
		return new BigDecimal(this.items.size() * 10);
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Payment getPayment() {
		return payment;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void finish() {
		status = "delivered";
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public Location getLocation() {
		return location;
	}

	public Item findItem(int id) {
		for(Item i : getItems()) {
			if(i.getId()==id) {
				return i;
			}
		}
		return null;
	}
	
}
