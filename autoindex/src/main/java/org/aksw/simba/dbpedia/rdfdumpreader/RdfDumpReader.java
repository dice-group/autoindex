package org.aksw.simba.dbpedia.rdfdumpreader;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;



public class RdfDumpReader {
	

	public static List<Resource> getResource()
	{
		FileManager.get().addLocatorClassLoader(RdfDumpReader.class.getClassLoader());
		Model model = FileManager.get().loadModel("instance_types_en.ttl", null, "TURTLE");
//		model.write(System.out, "TURTLE");
		List<Resource> listofResources =new ArrayList<Resource>();
		StmtIterator iter = model.listStatements();
		try {
			while (iter.hasNext()) {
				Statement stmt = iter.next();
			
			if(stmt.getResource().isURIResource())
			{
				listofResources.add(stmt.getResource());
//				System.out.println(stmt.getResource().getURI());
//				System.out.println(stmt.getResource().getLocalName());
////				
				
			}
	}

		} finally {
			if (iter != null)
				iter.close();
		}

		return listofResources;
	
	}
//	public static List<RDFNode> getObject()
//	{
//		FileManager.get().addLocatorClassLoader(RdfDumpReader.class.getClassLoader());
//		Model model = FileManager.get().loadModel("2015-10_dataid_en.ttl", null, "TURTLE");
////		model.write(System.out, "TURTLE");
//		List<RDFNode> listofResources =new ArrayList<RDFNode>();
//		StmtIterator iter = model.listStatements();
//		try {
//			while (iter.hasNext()) {
//				Statement stmt = iter.next();
//			Resource sub =stmt.getPredicate();
//			if(sub.isURIResource())
//			{
//			
//				listofResources.add(sub);
////				System.out.println(sub.getURI());
////				System.out.println(sub.getLocalName());
////				
//				
//			}
//			
//			}
//
//		} finally {
//			if (iter != null)
//				iter.close();
//		}
//
//		return listofResources;
//	} 
//	
//	public static List<Resource> getPredicate()
//	{
//		FileManager.get().addLocatorClassLoader(RdfDumpReader.class.getClassLoader());
//		Model model = FileManager.get().loadModel("2015-10_dataid_en.ttl", null, "TURTLE");
////		model.write(System.out, "TURTLE");
//		List<Resource> listofResources =new ArrayList<Resource>();
//		StmtIterator iter = model.listStatements();
//		try {
//			while (iter.hasNext()) {
//				Statement stmt = iter.next();
//			Resource sub =stmt.getPredicate();
//			if(sub.isResource())
//			{
//				listofResources.add(sub);
////				System.out.println(sub.getURI());
////				System.out.println(sub.getLocalName());
////				
//				
//			}
//			
//
//			
//			}
//
//		} finally {
//			if (iter != null)
//				iter.close();
//		}
//
//		return listofResources;
//	
//	}

	public static void main(String[] args) {
	
		List<Resource> sub_list = getResource(); 
//		List<Resource> pred_list = getPredicate(); 
//		List<RDFNode> obj_list= getObject();
		
		System.out.println("Subjects are --------------------------");
		for(Resource a: sub_list)
		{
			System.out.println("URI ::: "+ a.getURI());
			System.out.println("Label ::: "+ a.getLocalName());
		}
//		System.out.println("Objects are --------------------------");
//		for(RDFNode a: obj_list)
//		{
//			System.out.println("URI ::: "+ a.getURI());
//			System.out.println("Label ::: "+ a.getLocalName());
//		}
//		System.out.println("Perdicate are --------------------------");
//		for(Resource a: pred_list)
//		{
//			System.out.println("URI ::: "+ a.getURI());
//			System.out.println("Label ::: "+ a.getLocalName());
//		}
		
	}
	
}