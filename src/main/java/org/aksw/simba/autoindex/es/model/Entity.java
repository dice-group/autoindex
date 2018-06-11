package org.aksw.simba.autoindex.es.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@MappedSuperclass
@Document(indexName = "entity", type = "entity") //Create Programatically
public class Entity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	protected String url="";
	protected String label="";

	public Entity(String url, String label) {
		this.url=url;
		this.label=label;
		this.id = label + "_" + url;
	}
	public Entity() {
		this.url = "";
		this.label="";
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
}
