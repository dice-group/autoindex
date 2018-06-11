package org.aksw.simba.autoindex.datasource.custom;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.request.Keys;
import org.aksw.simba.autoindex.request.Keys.Category;
import org.aksw.simba.autoindex.request.Request;
import org.aksw.simba.autoindex.es.model.Property;
import org.aksw.simba.autoindex.es.model.DataClass;
public class CustomStringHandler<T>{
	 public ArrayList<T> indexInput(Request request) throws UnsupportedEncodingException{
		Keys keys = request.getKeys();
 		String label = keys.getFirstKey();
 		String url = keys.getSecondKey();
 	    ArrayList<String> indexed_list = new ArrayList<String>();
	 	Category index_type = keys.getCategory();
	 	switch(index_type) {
		case CLASS:
		{
 			ArrayList<DataClass> class_list = new ArrayList<DataClass>();
 			createClassArray(label, url,class_list);
 	 		indexed_list = (ArrayList<String>) class_list.clone();
 	 		break;
	     }
		case ENTITY:
		{
			ArrayList<Entity> entity_list = new ArrayList<Entity>();
			createEntityArray(label, url,entity_list);
			indexed_list = (ArrayList<String>) entity_list.clone();
			break;
		}
		case PROPERTY:
		{
 			ArrayList<Property> property_list = new ArrayList<Property>();
 			createPropertyArray(label, url,property_list);
 			indexed_list = (ArrayList<String>) property_list.clone();
 			break;
		}
		case NONE:
		default:
			break;
	 		
	 	}
 		
 		return (ArrayList<T>) indexed_list;
	 }
	 public void createPropertyArray(String label, String url, ArrayList<Property> property_list) {
	 		Property property = new Property(label,  url);
 			property_list.add(property);
	 }
	 
	 public void createEntityArray(String label, String url, ArrayList<Entity> entity_list) {
			Entity entity = new Entity(label,  url);
			entity_list.add(entity);
	 }
	 public void createClassArray(String label, String url, ArrayList<DataClass> class_list) {
	 		DataClass class_data = new DataClass(label,  url);
 	 		class_list.add(class_data);
	 }
}
