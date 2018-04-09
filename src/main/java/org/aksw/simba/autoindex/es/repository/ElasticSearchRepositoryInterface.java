/**
 * 
 */
package org.aksw.simba.autoindex.es.repository;

import org.aksw.simba.autoindex.es.model.Entity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/*For Autowiring*/

public interface ElasticSearchRepositoryInterface extends ElasticsearchRepository<Entity, String> {


}
