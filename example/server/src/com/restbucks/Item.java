package com.restbucks;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class Item {
	enum Coffee {latte, cappuccino, espresso};
	enum Milk {skim, semi, whole};
	enum Size {small, medium, large};

	private Coffee name;
	private int quantity;
	private  Milk milk;
	private Size size;
	
	public Item(Coffee name, int quantity, Milk milk, Size size) {
		this.name = name;
		this.quantity = quantity;
		this.milk = milk;
		this.size = size;
	}

	public Coffee getName() {
		return name;
	}

	public void setName(Coffee name) {
		this.name = name;
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
