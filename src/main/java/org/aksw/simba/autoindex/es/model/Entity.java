package org.aksw.simba.autoindex.es.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "entity", type = "entity")
public class Entity {
	@Id
	private String url;
	private String label;
	private Double pagerank;

	public Entity(String url, String label, Double pagerank) {
		this.setUrl(url);
		this.setLabel(label);
		this.setPagerank(pagerank);
	}

	public Entity(String string, String string2) {
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
