package br.com.caelum.example.infra;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.example.model.Item;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class Database {
	private int contador = 0;
	private final List<Item> items;

	public Database() {
		this.items = new ArrayList<Item>();
		this.adiciona(new Item("Chave", 20.0));
		this.adiciona(new Item("Lousa", 35.0));
	}

	public void adiciona(Item item) {
		item.setId(++contador);
		this.items.add(item);
	}

	public Item get(int id) {
		if(id > this.items.size()) {
			return null;
		}
		return this.items.get(id - 1);
	}

	public List<Item> lista() {
		return items;
	}

}
