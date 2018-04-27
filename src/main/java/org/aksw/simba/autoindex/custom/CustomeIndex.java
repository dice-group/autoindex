package org.aksw.simba.autoindex.custom;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.request.Keys;
public class CustomeIndex{
	 public ArrayList<Entity> makeKeys(Keys custom_input) throws UnsupportedEncodingException{
 		String label = custom_input.getFirstKey();
 		String url = custom_input.getSecondKey();
		ArrayList<Entity> entity_list = new ArrayList<Entity>();
		Entity entity = new Entity(label,  url);
		entity_list.add(entity);
		return entity_list;
 }
}