package br.com.caelum.restfulie.maze;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import br.com.caelum.restfulie.Link;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.http.Headers;

/**
 * 
 * @author jose donizetti
 */
public class Maze {

	public static void main(String[] args) {
		//stealing guilherme silveira algorithm
		Headers headers = Restfulie.at("http://amundsen.com/examples/mazes/2d/five-by-five/").accept("application/xml").get().getHeaders();
		
		Set<String> visited = new HashSet<String>();
		Stack<Link> path = new Stack<Link>();
		Link link = null;
		int steps = 0;
		
		while(!(headers.link("exit") != null)) {
			
			link = find(visited,"start north south east west",headers);
			
			if(link == null) {
				path.pop();
				link = path.pop();
			} 
			
			path.add(link);
			visited.add(link.getHref());
			System.out.println(link);
			headers = link.follow().get().getHeaders();
		
			steps++;
		}
		
		System.out.println("steps = " + steps);
		
	}

	private static Link find(Set<String> visited, String string, Headers headers) {
		String[] directions = string.split("\\s++");
		
		for(String direction : directions) {
			Link link = headers.link(direction);
			if((link != null) && (!visited.contains(link.getHref()))){
				return link;
			}
		}
		
		return null;
	}
	
}
