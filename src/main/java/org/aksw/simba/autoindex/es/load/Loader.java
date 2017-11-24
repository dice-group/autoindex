package org.aksw.simba.autoindex.es.load;
import java.util.Date;
import javax.annotation.PostConstruct;

import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.repository.EntityRespository;
import org.aksw.simba.autoindex.input.SparqlEndpointHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Loader {

	@Autowired
	ElasticsearchOperations operations;

	@Autowired
	EntityRespository esrepo;

	@PostConstruct
	@Transactional
	public void loadAll() {
		SparqlEndpointHandler sh = new SparqlEndpointHandler();
		operations.putMapping(Entity.class);
		Date d1 = new Date();
		int instances_limit = 40000;
		System.out.println("*************************************************Loading Data*************************************************");
		esrepo.save(sh.getResults(instances_limit));
		System.out.println(sh.getResults(instances_limit));
		System.out.println("*************************************************Loading Completed*************************************************");
		Date d2 = new Date();
		System.out.println("Records Limit = "+ instances_limit);
		
		getime(d1,d2);
	}
	
	public void getime(Date d1, Date d2) {

		long diff = d2.getTime() - d1.getTime();

		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		System.out.print(diffDays + " days, ");
		System.out.print(diffHours + " hours, ");
		System.out.print(diffMinutes + " minutes, ");
		System.out.print(diffSeconds + " seconds.");
	}

	

}
