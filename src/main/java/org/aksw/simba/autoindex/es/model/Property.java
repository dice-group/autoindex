package org.aksw.simba.autoindex.es.model;
import org.springframework.data.elasticsearch.annotations.Document;
@Document(indexName = "property", type = "property") 
public class Property extends Entity{
	public Property(String url, String label) {
		super(url, label);
	}
}
