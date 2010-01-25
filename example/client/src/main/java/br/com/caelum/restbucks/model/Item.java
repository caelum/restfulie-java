package br.com.caelum.restbucks.model;

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicitCollection;

@XStreamAlias("item")
public class Item {
	enum Coffee {
		latte(2.0), cappuccino(2.0), espresso(1.5);
		private final BigDecimal price;

		Coffee(double price) {
			this.price = new BigDecimal(price);
		}
	}

	enum Milk {
		skim, semi, whole
	};

	enum Size {
		small, medium, large
	};

	private Coffee name;
	private int quantity;
	private Milk milk;
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

	public BigDecimal getPrice() {
		return name.price;
	}

}
