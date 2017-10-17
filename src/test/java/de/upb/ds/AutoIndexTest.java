package de.upb.ds;
import java.util.List;

import org.aksw.simba.autoindex.input.SparqlEndpointHandler;
import org.apache.jena.ext.com.google.common.base.Joiner;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AutoIndexTest extends TestCase {
	private String endpoint= "http://dbpedia.org/sparql";;
	private SparqlEndpointHandler seh = new SparqlEndpointHandler();
	
	public AutoIndexTest(String testName) {
		super(testName);
	}
	protected void setUp() throws Exception {
		super.setUp();
	}
	public void testgetallclasses() {
		seh.setLang("en");
		ResultSet results = seh.getallclasses(endpoint);
		assertNotNull(results);
		List<QuerySolution> asText = ResultSetFormatter.toList(results);
		Joiner listOfResults = Joiner.on("\n");
		System.out.println("Classes Results" +listOfResults.join(asText));
		System.out.println("Total Visible Results = " + asText.size());
		Assert.assertTrue(asText.size() >1);
	}
	
	public void testGetAllClassMultilingual() {
		seh.setLang("de");
		ResultSet results = seh.getallclasses(endpoint);
		assertNotNull(results);
		List<QuerySolution> asText = ResultSetFormatter.toList(results);
		System.out.println("Deutsch Language Results" +Joiner.on("\n").join(asText));
		System.out.println("Total Visible Results " + asText.size());
		Assert.assertTrue(asText.size() >1);

		seh.setLang("en");

		results = seh.getallclasses(endpoint);
		assertNotNull(results);
		asText = ResultSetFormatter.toList(results);
		System.out.println("English Language Results" +Joiner.on("\n").join(asText));
		System.out.println("Total Visible Results " + asText.size());
		Assert.assertTrue(asText.size() >1);

//		System.out.println("Classes"+ResultSetFormatter.toList(results).size());
		
	}
	public void testgetallinstances() {
		seh.setLang("en");

		ResultSet sum = seh.getallinstances(endpoint);
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
		Assert.assertTrue(asText.size() >1);
		
		
	}
	public void testallproperties() {
		seh.setLang("en");

		ResultSet sum = seh.getallproperties(endpoint);
	    assertNotNull(sum);

	    List<QuerySolution> asText = ResultSetFormatter.toList(sum);
		System.out.println("Properties Results" +Joiner.on("\n").join(asText));
		System.out.println("Total Visible Results = " + asText.size());
		Assert.assertTrue(asText.size() >1);

	}
}
