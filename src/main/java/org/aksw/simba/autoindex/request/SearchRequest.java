package org.aksw.simba.autoindex.request;

import org.aksw.simba.autoindex.request.Keys.Category;

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
		if("uri".equals(type) || "URI".equals(type) || "Uri".equals(type))
			this.type = Type.URI;
		else if ("label".equals(type) || "LABEL".equals(type) || "Label".equals(type))
			this.type = Type.LABEL;
		else
			this.type = Type.NONE;
	}
	
	public void setCategory(String category) {
		if("CLASS".equals(category) || "class".equals(category) || "Class".equals(category))
			this.category = Category.CLASS;
		else if ("PROPERTY".equals(category) || "property".equals(category) || "Property".equals(category))
			this.category = Category.PROPERTY; 
		else if ("ENTITY".equals(category) || "entity".equals(category) || "Entity".equals(category))
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