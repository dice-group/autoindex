package org.aksw.simba.autoindex.request;

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
		String type = category.toLowerCase();
		if("class".equals(type))
			this.category = Category.CLASS;
		else if ("property".equals(type))
			this.category = Category.PROPERTY; 
		else if ("entity".equals(type))
			this.category = Category.ENTITY;
		else
			this.category = Category.NONE;
	}
	
	public Category getCategory(){
		return this.category;
	}

}