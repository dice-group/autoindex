package org.aksw.simba.autoindex.response;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DataTest {
	@Test 
	public void checkParameters()  {
		Data data = new Data();
		data.setType("literal");
		data.setValue("Obama");
		assertTrue("Obama".equals(data.getValue()));
		assertTrue("literal".equals(data.getType()));
	}
}
