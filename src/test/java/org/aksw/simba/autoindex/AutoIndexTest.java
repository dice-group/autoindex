/*package org.aksw.simba.autoindex;
import java.util.List;

import org.aksw.simba.autoindex.sparql.*;
import org.apache.jena.ext.com.google.common.base.Joiner;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AutoIndexTest extends TestCase {
	private String endpoint= "http://dbpedia.org/sparql";;
	private SparqlHandler seh = new SparqlHandler();
	
	public AutoIndexTest(String testName) {
		super(testName);
	}
	protected void setUp() throws Exception {
		super.setUp();
	}
	public void testgetallclasses() {
		seh.setLang("en");
		seh.setBaseUri(endpoint);
		ResultSet results = seh.getallclasses();
		assertNotNull(results);
		List<QuerySolution> asText = ResultSetFormatter.toList(results);
		Joiner listOfResults = Joiner.on("\n");
		System.out.println("Classes Results" +listOfResults.join(asText));
		System.out.println("Total Visible Results = " + asText.size());
		Assert.assertTrue(asText.size() >1);
	}
	
	public void testGetAllClassMultilingual() {
		seh.setLang("de");
		seh.setBaseUri(endpoint);
		ResultSet results = seh.getallclasses();
		assertNotNull(results);
		List<QuerySolution> asText = ResultSetFormatter.toList(results);
		System.out.println("Deutsch Language Results" +Joiner.on("\n").join(asText));
		System.out.println("Total Visible Results " + asText.size());
		Assert.assertTrue(asText.size() >1);
		seh.setLang("en");
		results = seh.getallclasses();
		assertNotNull(results);
		asText = ResultSetFormatter.toList(results);
		System.out.println("English Language Results" +Joiner.on("\n").join(asText));
		System.out.println("Total Visible Results " + asText.size());
		Assert.assertTrue(asText.size() >1);
//
////		System.out.println("Classes"+ResultSetFormatter.toList(results).size());
		
	}
	
	
	public void testgetallinstances() {
		int instances_limit = 1;
		seh.setLang("en");
		seh.setBaseUri(endpoint);
		ResultSet sum = seh.getallinstances(instances_limit);
		assertNotNull(sum);
		List<QuerySolution> asText = ResultSetFormatter.toList(sum);
		System.out.println("Instances Reults" +Joiner.on("\n").join(asText));
		System.out.println("Total Visible Results = " + asText.size());

		//		System.out.println(ResultSetFormatter.asText(results));
//		ArrayList<Entity> entity_list = new ArrayList<Entity>();
//		while (sum.hasNext()) {
//			QuerySolution qs = sum.next();
//			entity_list.add(new Entity(qs.getResource("type").getURI(), qs
//					.getLiteral("label").getString(), Double.parseDouble(qs
//					.getLiteral("v").getString())));
//
//		}
		Assert.assertTrue(asText.size() == instances_limit);
		
		
	}
	public void testallproperties() {
		seh.setLang("en");
		seh.setBaseUri(endpoint);
		ResultSet sum = seh.getallproperties();
	    assertNotNull(sum);

	    List<QuerySolution> asText = ResultSetFormatter.toList(sum);
		System.out.println("Properties Results" +Joiner.on("\n").join(asText));
		System.out.println("Total Visible Results = " + asText.size());
		Assert.assertTrue(asText.size() >1);

	}
}*/
