package org.aksw.simba.autoindex.request;

import static org.junit.Assert.assertTrue;

import org.aksw.simba.autoindex.request.SearchRequest.Category;
import org.aksw.simba.autoindex.request.SearchRequest.Type;
import org.junit.Test;

public class SearchRequestTest {
	@Test 
	public void checkSearchParameters()  {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.setQuery("Who is Obama");
		assertTrue("Who is Obama".equals(searchRequest.getQuery()));
		searchRequest.setType("uri");
		assertTrue(Type.URI.equals(searchRequest.getType()));
		searchRequest.setType("label");
		assertTrue(Type.LABEL.equals(searchRequest.getType()));
		searchRequest.setType("garbage");
		assertTrue(Type.NONE.equals(searchRequest.getType()));
		
		searchRequest.setCategory("entity");
		assertTrue(Category.ENTITY.equals(searchRequest.getCategory()));
		searchRequest.setCategory("property");
		assertTrue(Category.PROPERTY.equals(searchRequest.getCategory()));
		searchRequest.setCategory("class");
		assertTrue(Category.CLASS.equals(searchRequest.getCategory()));
		searchRequest.setCategory("all");
		assertTrue(Category.ALL.equals(searchRequest.getCategory()));
	}
}
