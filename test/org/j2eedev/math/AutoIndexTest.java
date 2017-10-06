package org.j2eedev.math;
import java.util.ArrayList;

import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.input.SparqlEndpointHandler;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

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
		System.out.println(ResultSetFormatter.asText(results));
		
		
	}
	public void testgetallinstances() {
		ResultSet sum = SparqlEndpointHandler.getallinstances(endpoint);
		assertNotNull(sum);
//		System.out.println(ResultSetFormatter.toList(sum).size());
		System.out.println(ResultSetFormatter.asText(sum));
//		System.out.println(ResultSetFormatter.asText(results));
//		ArrayList<Entity> entity_list = new ArrayList<Entity>();
//		while (sum.hasNext()) {
//			QuerySolution qs = sum.next();
//			entity_list.add(new Entity(qs.getResource("type").getURI(), qs
//					.getLiteral("label").getString(), Double.parseDouble(qs
//					.getLiteral("v").getString())));
//
//		}
//		
//		Assert.assertTrue(ResultSetFormatter.toList(sum).size() >1);
		
		
	}
	public void testallproperties() {
		ResultSet sum = SparqlEndpointHandler.getallproperties(endpoint);
	    assertNotNull(sum);
	    System.out.println(ResultSetFormatter.asText(sum));
	}
}
