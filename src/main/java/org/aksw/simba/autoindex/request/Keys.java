package org.aksw.simba.autoindex.request;

import org.aksw.simba.autoindex.request.SearchRequest.Category;

public class Keys{
	
	private String firstKey;
	private String secondKey;
	private Category category;
	public enum Category{
		CLASS,
		ENTITY,
		PROPERTY,
		NONE
	};
	public String getFirstKey() {
		return this.firstKey;
	}
	
	public void setFirstKey(String firstKey) {
		this.firstKey = firstKey;
	}
	
	public String getSecondKey() {
		return this.secondKey;
	}
	
	public void setSecondKey(String secondKey) {
		this.secondKey = secondKey;
	}
	
	public void setCategory(String category) {
		if("CLASS".equals(category) || "class".equals(category) || "Class".equals(category))
			this.category = Category.CLASS;
		else if ("PROPERTY".equals(category) || "property".equals(category) || "Property".equals(category))
			this.category = Category.PROPERTY; 
		else if ("ENTITY".equals(category) || "entity".equals(category) || "Entity".equals(category))
			this.category = Category.ENTITY;
		else
			this.category = Category.NONE;
	}
	
	public Category getCategory(){
		return this.category;
	}

}