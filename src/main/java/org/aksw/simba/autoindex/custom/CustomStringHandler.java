package org.aksw.simba.autoindex.custom;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.request.Keys;
import org.aksw.simba.autoindex.request.Request;
import org.aksw.simba.autoindex.es.model.Property;
import org.aksw.simba.autoindex.es.model.DataClass;

public class CustomStringHandler<T>{
	 public ArrayList<T> indexInput(Request request) throws UnsupportedEncodingException{
		 
		Keys keys = request.getKeys();
 		String label = keys.getFirstKey();
 		String url = keys.getSecondKey();
 	    ArrayList<String> indexed_list = new ArrayList<String>();
	 	String index_type = keys.getThirdKey();
 		if(index_type.equals("class")) {
 			ArrayList<DataClass> class_list = new ArrayList<DataClass>();
 	 		DataClass class_data = new DataClass(label,  url);
 	 		class_list.add(class_data);
 	 		indexed_list = (ArrayList<String>) class_list.clone();
	
 	     }
 		else if(index_type.equals("entity")) {
 			ArrayList<Entity> entity_list = new ArrayList<Entity>();
 			Entity entity = new Entity(label,  url);
 			entity_list.add(entity);
 			indexed_list = (ArrayList<String>) entity_list.clone();
 		}
 		else if(index_type.equals("property")) {
 			ArrayList<Property> property_list = new ArrayList<Property>();
 	 		Property property = new Property(label,  url);
 			property_list.add(property);
 			indexed_list = (ArrayList<String>) property_list.clone();
 		}
 		return (ArrayList<T>) indexed_list;
 }
	 
}