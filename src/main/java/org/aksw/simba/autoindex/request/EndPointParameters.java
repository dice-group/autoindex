package org.aksw.simba.autoindex.request;

public class EndPointParameters{
	private String url = "";
	private Boolean isEntityCustomized = false;
	private Boolean isPropertyCustomized = false;
	private Boolean isClassCustomized = false;
	private String entitySelectQuery = "";
	private String propertySelectQuery = "";
	private String classSelectQuery = "";
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setIsEntityCustomized(Boolean isEntityCustomized) {
		this.isEntityCustomized = isEntityCustomized;
	}
	
	public void setIsPropertyCustomized(Boolean isPropertyCustomized) {
		this.isPropertyCustomized = isEntityCustomized;
	}
	
	public void setIsClassCustomized(Boolean isClassCustomized) {
		this.isClassCustomized = isEntityCustomized;
	}
	
	public void setClassSelectQuery(String classSelectQuery) {
		this.classSelectQuery = classSelectQuery;
	}
	
	public void setPropertySelectQuery(String propertySelectQuery) {
		this.propertySelectQuery = propertySelectQuery;
	}
	
	public void setEntitySelectQuery(String entitySelectQuery) {
		this.entitySelectQuery = entitySelectQuery;
	}
	
	public Boolean getIsClassCustomized() {
		return this.isClassCustomized;
	}
	
	public Boolean getIsPropertyCustomized() {
		return this.isPropertyCustomized;
	}
	
	public Boolean getIsEntityCustomized() {
		return this.isEntityCustomized;
	}
	
	public String getEntitySelectQuery() {
		return this.entitySelectQuery;
	}
	
	public String getPropertySelectQuery() {
		return this.propertySelectQuery;
	}
	
	public String getClassSelectQuery() {
		return this.classSelectQuery;
	}
	
}