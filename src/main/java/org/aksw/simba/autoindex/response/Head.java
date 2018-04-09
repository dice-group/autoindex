package org.aksw.simba.autoindex.response;

import java.util.ArrayList;
import java.util.List;

public class Head{
	private List<String> vars = new ArrayList<>();
	
	public void add(String entry) {
		this.vars.add(entry);
	}
	
	public List<String> getVars() {
		return this.vars;
	}
}