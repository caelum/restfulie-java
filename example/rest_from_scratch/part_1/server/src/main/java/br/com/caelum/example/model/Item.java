package br.com.caelum.example.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class Item {
	private Integer id;
	private String name;
	private Double price;

	public Item(String nome, Double preco) {
		this.name = nome;
		this.price = preco;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String nome) {
		this.name = nome;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double preco) {
		this.price = preco;
	}

}
