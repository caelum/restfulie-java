package br.com.caelum.vraptor.restbucks;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class Item {
	enum Coffee {LATTE, CAPPUCINO, ESPRESSO};
	enum Milk {SKIM, SEMI, WHOLE};
	enum Size {SMALL, MEDIUM, LARGE};

	private Coffee drink;
	private int quantity;
	private  Milk milk;
	private Size size;
	
	public Item(Coffee name, int quantity, Milk milk, Size size) {
		this.drink = name;
		this.quantity = quantity;
		this.milk = milk;
		this.size = size;
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

}
