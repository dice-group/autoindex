package org.aksw.simba.autoindex.es.model;


import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "class", type = "class") 
public class DataClass extends Entity{

	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String url;
	private String label;
	
	public DataClass(String url, String label) {
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
	
	public DataClass() {
		this.url="";
		this.label="";
	}

}
