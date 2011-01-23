package br.com.caelum.restfulie.maze;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.junit.Test;

import br.com.caelum.restfulie.Link;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.http.Headers;

public class MazeTest {

	public static void main(String[] args) {
		//stealing guilherme silveira algorithm
		Headers headers = Restfulie.at("http://amundsen.com/examples/mazes/2d/five-by-five/").accept("application/xml").get().getHeaders();
		
		Map<String,String> visited = new HashMap<String,String>();
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
			visited.put(link.getHref(), link.getRel());
			System.out.println(link);
			headers = link.follow().get().getHeaders();
		
			steps++;
		}
		
		System.out.println("steps = " + steps);
		
	}

	private static Link find(Map<String, String> visited, String string, Headers headers) {
		String[] directions = string.split("\\s++");
		
		for(String direction : directions) {
			Link link = headers.link(direction);
			if((link != null) && (!visited.containsKey(link.getHref()))){
				return link;
			}
		}
		
		return null;
	}
	
}
