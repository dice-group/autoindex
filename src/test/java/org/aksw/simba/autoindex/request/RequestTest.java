package org.aksw.simba.autoindex.request;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.aksw.simba.autoindex.request.Keys.Category;
import org.aksw.simba.autoindex.request.Request.RequestType;
import org.junit.Test;

public class RequestTest {
	
	@Test 
	public void checkEndPointParameters()  {
		Request request = new Request();
		EndPointParameters endPointParameters = request.getEndPointParameters();
		assertTrue(endPointParameters != null);
		endPointParameters.setUrl("http://dbpedia.org/sparql");
		assertTrue("http://dbpedia.org/sparql".equals(endPointParameters.getUrl()));
		assertFalse(endPointParameters.getIsClassCustomized());
		assertFalse(endPointParameters.getIsEntityCustomized());
		assertFalse(endPointParameters.getIsPropertyCustomized());
	}
	
	@Test 
	public void checkCustomEndPointQueries() {
		Request request = new Request();
		EndPointParameters endPointParameters = request.getEndPointParameters();
		assertTrue(endPointParameters != null);
		endPointParameters.setUrl("http://dbpedia.org/sparql");
		endPointParameters.setIsEntityCustomized(true);
		endPointParameters.setEntitySelectQuery("ABC");
		assertFalse(endPointParameters.getIsClassCustomized());
		assertTrue(endPointParameters.getIsEntityCustomized());
		assertFalse(endPointParameters.getIsPropertyCustomized());
		endPointParameters.setIsEntityCustomized(false);
		endPointParameters.setIsPropertyCustomized(true);
		endPointParameters.setPropertySelectQuery("ABC");
		assertFalse(endPointParameters.getIsClassCustomized());
		assertFalse(endPointParameters.getIsEntityCustomized());
		assertTrue(endPointParameters.getIsPropertyCustomized());
		endPointParameters.setIsPropertyCustomized(false);
		endPointParameters.setIsClassCustomized(true);
		endPointParameters.setClassSelectQuery("ABC");
		assertTrue(endPointParameters.getIsClassCustomized());
		assertFalse(endPointParameters.getIsEntityCustomized());
		assertFalse(endPointParameters.getIsPropertyCustomized());
		assertTrue("ABC".equals(endPointParameters.getClassSelectQuery()));
		assertTrue("ABC".equals(endPointParameters.getEntitySelectQuery()));
		assertTrue("ABC".equals(endPointParameters.getPropertySelectQuery()));
	}
	
	@Test
	public void testRequestParameters() {
		ArrayList<String> fileList = new ArrayList<String>();
		fileList.add("file.ttl");
		fileList.add("file1.ttl");
		Request request = new Request();
		request.setDefaultGraph("sask");
		request.setLimit(100);
		request.setFileList(fileList);
		assertTrue(request.getlimit() == 100);
		assertTrue("sask".equals(request.getDefaultGraph()));
		assertTrue(request.getFileList().size() == 2);
		assertTrue("file.ttl".equals(request.getFileList().get(0)));
		assertTrue("file1.ttl".equals(request.getFileList().get(1)));
		Keys keys = request.getKeys();
		assertTrue(keys != null);
		keys.setCategory("class");
		keys.setFirstKey("ABC");
		keys.setSecondKey("DEF");
		assertTrue(Category.CLASS.equals(keys.getCategory()));
		keys.setCategory("property");
		assertTrue(Category.PROPERTY.equals(keys.getCategory()));
		keys.setCategory("entity");
		assertTrue(Category.ENTITY.equals(keys.getCategory()));
		keys.setCategory("garbage");
		assertTrue(Category.NONE.equals(keys.getCategory()));
		assertTrue("ABC".equals(keys.getFirstKey()));
		assertTrue("DEF".equals(keys.getSecondKey()));
		assertFalse(request.isUseLocalDataSource());
		assertTrue(RequestType.NONE.equals(request.getRequestType()));
		request.setUseLocalDataSource(true);
		assertTrue(request.isUseLocalDataSource());
		assertTrue(RequestType.LOCAL_DB.equals(request.getRequestType()));
		request.setUseLocalDataSource(false);
		//Check Request types.
	
		request.setRequestType("uri");
		assertTrue(RequestType.URI.equals(request.getRequestType()));
		request.setRequestType("filePath");
		assertTrue(RequestType.RDF_FILE.equals(request.getRequestType()));
		request.setRequestType("custom");
		assertTrue(RequestType.CUSTOM_STRING.equals(request.getRequestType()));
		request.setRequestType("localdb");
		assertTrue(RequestType.LOCAL_DB.equals(request.getRequestType()));
		assertTrue(request.isUseLocalDataSource());
		
		request.setUserId("1111");
		assertTrue("1111".equals(request.getUserId()));
	}
}
