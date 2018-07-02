package org.aksw.simba.autoindex.response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ResponseTest {
	@Test 
	public void checkParameters()  {
		Response response = new Response();
		assertFalse(response.getBoolean());
		response.setBoolean(true);
		assertTrue(response.getBoolean());
		Head head = response.getHead();
		assertTrue(head != null);
		Results results = response.getResults();
		assertTrue(results!= null);
		List<Bindings> bindings = results.getBindings();
		assertTrue(bindings.isEmpty());
		
	}
}
