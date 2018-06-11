package org.aksw.simba.autoindex.es.model;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "class", type = "class") 
public class DataClass extends Entity{	
	public DataClass(String url, String label) {
		super(url, label);
	}
}
