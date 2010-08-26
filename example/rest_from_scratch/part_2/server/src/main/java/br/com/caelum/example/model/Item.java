package br.com.caelum.example.model;

import br.com.caelum.example.controller.ItemsController;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaResource;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class Item implements HypermediaResource {
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

	@Override
	public void configureRelations(RelationBuilder builder) {
		builder.relation("self").uses(ItemsController.class).show(id);
	}

}
