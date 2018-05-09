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
				classArray(label, url,class_list);
	 	 		indexed_list = (ArrayList<String>) class_list.clone();
 	     }
 		else if(index_type.equals("entity")) {
 				ArrayList<Entity> entity_list = new ArrayList<Entity>();
				entityArray(label, url,entity_list);
				indexed_list = (ArrayList<String>) entity_list.clone();
 		}
 		else if(index_type.equals("property")) {
 	 			ArrayList<Property> property_list = new ArrayList<Property>();
				propertyArray(label, url,property_list);
	 			indexed_list = (ArrayList<String>) property_list.clone();
 		}
 		return (ArrayList<T>) indexed_list;
	 }
	 public ArrayList<Property> propertyArray(String label, String url, ArrayList<Property> property_list) {
	 		Property property = new Property(label,  url);
 			property_list.add(property);
			return property_list;
	 }
	 public ArrayList<Entity> entityArray(String label, String url, ArrayList<Entity> entity_list) {
			Entity entity = new Entity(label,  url);
			entity_list.add(entity);
			return entity_list;
	 }
	 public ArrayList<DataClass> classArray(String label, String url, ArrayList<DataClass> class_list) {
	 		DataClass class_data = new DataClass(label,  url);
 	 		class_list.add(class_data);
			return class_list;
	 }
}