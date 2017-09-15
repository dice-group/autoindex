package org.aksw.simba.autoindex.es.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "entity", type = "entity")
public class Entity {
	@Id
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String url;

	private String label;
	private Double pagerank;

	public Entity(String url, String label, Double pagerank) {
		this.setUrl(url);
		this.setLabel(label);
		this.setPagerank(pagerank);
	}

	public Entity(String url, String label) {
		this.setUrl(url);
		this.setLabel(label);
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

	public Double getPagerank() {
		return pagerank;
	}

	public void setPagerank(Double pagerank) {
		this.pagerank = pagerank;
	}

	public Entity() {
	}

}
