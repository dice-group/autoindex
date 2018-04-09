package org.aksw.simba.autoindex.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Results{
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Bindings bindings;
	
	public Bindings getBindings() {
		if (bindings == null) {
			return new Bindings();
		}
		return this.bindings;
	}
	
	public Results(){
		this.bindings = new Bindings();
	}
	
}