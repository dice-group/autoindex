package org.aksw.simba.autoindex.request;

public class SearchRequest{
	String query;
	
	public enum Type {
		LABEL ,
		URI  ,
		NONE
	};
	
	public enum Category{
		CLASS,
		ENTITY,
		PROPERTY,
		ALL,
		NONE
	};
	
	Type type;
	Category category;
	
	public SearchRequest(){
		this.query = "";
		this.type = Type.NONE;
		this.category = Category.NONE;
	}
	
	public SearchRequest(String query){
		this.query = query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public void setType(String type) {
		String varType = type.toLowerCase();
		if("uri".equals(varType))
			this.type = Type.URI;
		else if ("label".equals(varType))
			this.type = Type.LABEL;
		else
			this.type = Type.NONE;
	}
	
	public void setCategory(String category) {
		String type = category.toLowerCase();
		if("class".equals(type))
			this.category = Category.CLASS;
		else if ("property".equals(type))
			this.category = Category.PROPERTY; 
		else if ("entity".equals(type))
			this.category = Category.ENTITY;
		else
			this.category = Category.ALL;
	}
	
	public Category getCategory(){
		return this.category;
	}
	
	public Type getType() {
		return this.type;
	}
	
}