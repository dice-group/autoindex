/**
 * 
 */
package org.aksw.simba.autoindex.es.repository;

import org.aksw.simba.autoindex.es.model.Entity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Prashanth
 * Require this class for autowiring Elastic Search Repository. 
 * Could not find anything on the internet to avoid this so far!
 *
 */
public interface ElasticSearchRepositoryInterface extends ElasticsearchRepository<Entity, String> {

}
