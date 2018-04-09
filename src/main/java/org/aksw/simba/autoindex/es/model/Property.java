package org.aksw.simba.autoindex.es.model;


import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "property", type = "property") 
public class Property extends Entity{

	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String url;
	private String label;
	
	public Property(String url, String label) {
		this.url = url;
		this.label=label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public Property() {
		this.url="";
		this.label="";
	}

}
