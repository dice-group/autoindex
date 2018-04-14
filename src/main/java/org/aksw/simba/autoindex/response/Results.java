package org.aksw.simba.autoindex.response;

import java.util.ArrayList;
import java.util.List;

public class Results{
	
	private List<Bindings> bindings = new ArrayList<Bindings>();
	
	public List<Bindings> getBindings() {
		return this.bindings;
	}
	
	public Results(){
	}
	
	public void addBinding(Bindings bindings) {
		this.bindings.add(bindings);
	}
	
}