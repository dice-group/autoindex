package org.aksw.simba.autoindex.response;

public class Data{
	
	private String type;
	private String value;
	
	public Data(){
		this.type="";
		this.value="";
	}
	
	public Data(String type , String value){
		this.type = type;
		this.value = value;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}