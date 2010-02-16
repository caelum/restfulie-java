package br.com.caelum.vraptor.restbucks;

import java.util.List;

import br.com.caelum.vraptor.restbucks.web.ItemController;
import br.com.caelum.vraptor.restfulie.Restfulie;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaResource;
import br.com.caelum.vraptor.restfulie.relation.Relation;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class Item implements HypermediaResource{
	enum Coffee {LATTE, CAPPUCINO, ESPRESSO};
	enum Milk {SKIM, SEMI, WHOLE};
	enum Size {SMALL, MEDIUM, LARGE};

	private Coffee drink;
	private int quantity;
	private  Milk milk;
	private Size size;
	private int id;
	
	private transient Order order;
	
	public Item(Coffee drink, int quantity, Milk milk, Size size) {
		this.drink = drink;
		this.quantity = quantity;
		this.milk = milk;
		this.size = size;
	}
	Item() {
		
	}
	
	public void use(Order order, int id) {
		this.order = order;
		this.id = id;
	}

	public Coffee getDrink() {
		return drink;
	}

	public void setDrink(Coffee name) {
		this.drink = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Milk getMilk() {
		return milk;
	}

	public void setMilk(Milk milk) {
		this.milk = milk;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public List<Relation> getRelations(Restfulie control) {
		control.relation("self").uses(ItemController.class).get(order, this);
		return control.getRelations();
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
