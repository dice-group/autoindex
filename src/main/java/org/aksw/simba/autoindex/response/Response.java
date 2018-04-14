package org.aksw.simba.autoindex.response;


public class Response{
	
	private Head head ;
	private boolean Boolean = false;
	private Results results;
	
	public Head getHead() {
		if (this.head == null) {
			return new Head();
		}
		return this.head;
	}
	
	public void setHead(Head head) {
		this.head = head;
	}
	
	public void setBoolean(boolean Boolean) {
		this.Boolean = Boolean;
	}
	
	public boolean getBoolean() {
		return this.Boolean;
	}
	//TODO: for search()
	public Results getResults() {
		if (this.results == null) {
			return new Results();
		}
		return this.results;
	}
	
	public void setResults(Results results) {
		this.results = results;
	}
	
}