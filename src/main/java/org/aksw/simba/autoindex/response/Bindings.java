package org.aksw.simba.autoindex.response;


public class Bindings{
	
	private Data label;
	private Data uri;
	
	public Bindings() {
		this.label = new Data();
		this.uri = new Data();
	}
	
	public Data getLabel(){
		return this.label;
	}
	
	public Data getUri(){
		return this.uri;
	}
	
	public Bindings(Data label , Data uri) {
		this.label = label;
		this.uri = uri;
	}
	
	public void setLabel(String label) {
		this.label.setType("literal");
		this.label.setValue(label);
	}
	
	public void setUri(String uri) {
		this.uri.setType("uri");
		this.uri.setValue(uri);
	}
}