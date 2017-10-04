package org.j2eedev.math;
import java.util.ArrayList;

import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.input.SparqlEndpointHandler;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AutoIndexTest extends TestCase {
	private String endpoint= "http://dbpedia.org/sparql";;
	public AutoIndexTest(String testName) {
		super(testName);
	}
	protected void setUp() throws Exception {
		super.setUp();
	}
	public void testgetallclasses() {
		ResultSet results = SparqlEndpointHandler.getallclasses(endpoint);
		assertNotNull(results);
		
		ArrayList<Entity> entity_list = new ArrayList<Entity>();
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			entity_list.add(new Entity(qs.getResource("type").getURI(), qs
					.getLiteral("label").getString(), Double.parseDouble(qs
					.getLiteral("v").getString())));

		}
		
		Assert.assertTrue(entity_list.size()>100000);

		
		System.out.println("____________________________");
		System.out.println(results);
		System.out.println("____________________________");
	}
	public void testgetallinstances() {
		ResultSet sum = SparqlEndpointHandler.getallinstances(endpoint);
		assertNotNull(sum);
	}
	public void testallproperties() {
		ResultSet sum = SparqlEndpointHandler.getallproperties(endpoint);
		assertNotNull(sum);
	}
}
