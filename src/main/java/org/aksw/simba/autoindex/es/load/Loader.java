package org.aksw.simba.autoindex.es.load;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.repository.EntityRepository;
import org.aksw.simba.autoindex.input.SparqlEndpointHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Loader {
	private static final Logger log = LoggerFactory
            .getLogger(Loader.class);
	@Autowired
	ElasticsearchOperations operations;

	@Autowired
	EntityRepository repository;

	@PostConstruct
	@Transactional
	public void loadAll() {
		SparqlEndpointHandler sh = new SparqlEndpointHandler();
		operations.putMapping(Entity.class);
		Date d1 = new Date();
		int instances_limit = 10;
		log.warn("*************************************************Loading Data*************************************************");
		repository.save(sh.getResults(instances_limit));
		log.warn( sh.getResults(instances_limit).toString());
		log.warn("*************************************************Loading Completed*************************************************");
		Date d2 = new Date();
		log.warn("Records Limit = %s" , instances_limit);
		
		getime(d1,d2);
	}
	
	public void getime(Date d1, Date d2) {

		long diff = d2.getTime() - d1.getTime();

		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		log.warn("%s  days, " , diffDays);
		log.warn(diffHours + " hours, ");
		log.warn(diffMinutes + " minutes, ");
		log.warn(diffSeconds + " seconds.");
	}

	

}
