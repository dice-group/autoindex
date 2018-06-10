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
		if(category.equals("CLASS") || category.equals("class") || category.equals("Class"))
			this.category = Category.CLASS;
		else if (category.equals("PROPERTY") || category.equals("property") || category.equals("Property"))
			this.category = Category.PROPERTY; 
		else if (category.equals("ENTITY") || category.equals("entity") || category.equals("Entity"))
			this.category = Category.ENTITY;
		else
			this.category = Category.NONE;
	}
	
	public Category getCategory(){
		return this.category;
	}

}