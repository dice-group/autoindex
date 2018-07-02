package org.aksw.simba.autoindex.response;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BindingsTest {
	@Test 
	public void checkParameters()  {
		Bindings bindings = new Bindings();
		bindings.setLabel("Obama");
		bindings.setUri("http://obama.com");
		assertTrue("Obama".equals(bindings.getLabel().getValue()));
		assertTrue("http://obama.com".equals(bindings.getUri().getValue()));
		assertTrue("literal".equals(bindings.getLabel().getType()));
		assertTrue("uri".equals(bindings.getUri().getType()));
	}
}
