package de.lehsten.casa.contextserver.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.place.Stop;

public class EntityTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void cloneTest() {
		Entity e1 =  new Entity();
		HashMap<String,String> properties = new HashMap<String,String>();
		e1.setSource("Source1");
		properties.put("key1", "value1");
		properties.put("key2", "value2");
		properties.put("url", "http://www.eee.cde");
		e1.setProperties(properties);
		Entity e2 =  new Entity();
		e2 = e1.clone();
		System.out.println(e1);
		System.out.println(e2);
		assertTrue(e1 != e2 && e1.equals(e2) && e1.getClass().equals(e2.getClass()));
		assertTrue(e1.equals(e1.clone()));
	}

}
