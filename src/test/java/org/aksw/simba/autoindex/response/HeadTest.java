package org.aksw.simba.autoindex.response;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HeadTest {
	@Test 
	public void checkParameters()  {
		Head head = new Head();
		head.add("one");
		assertTrue(head.getVars().size() == 1);
		assertTrue("one".equals(head.getVars().get(0)));
		head.add("two");
		assertTrue(head.getVars().size() == 2);
		assertTrue("one".equals(head.getVars().get(0)));
		assertTrue("two".equals(head.getVars().get(1)));
	}
}
