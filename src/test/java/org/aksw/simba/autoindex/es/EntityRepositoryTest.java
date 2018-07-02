package org.aksw.simba.autoindex.es;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.autoindex.datasource.file.FileHandler;
import org.aksw.simba.autoindex.datasource.sparql.SparqlHandler;
import org.aksw.simba.autoindex.es.model.DataClass;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.model.Property;
import org.aksw.simba.autoindex.es.repository.ElasticSearchRepositoryInterface;
import org.aksw.simba.autoindex.es.repository.EntityRepository;
import org.aksw.simba.autoindex.request.EndPointParameters;
import org.aksw.simba.autoindex.request.Request;
import org.aksw.simba.autoindex.request.SearchRequest;
import org.aksw.simba.autoindex.request.SearchRequest.Category;
import org.aksw.simba.autoindex.request.SearchRequest.Type;
import org.aksw.simba.autoindex.response.Response;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

//Tried Spring Runner, SpringBoot test, Enabling JPA Repositores, using a custom version of ESTemplate and also a custom Config file with varying ComponentScan.
//All mostly dont autowire ElasticSearchRepositoryInterface
//Additionally capturing argument on a mocked interface works unreliably. So commented out FileHandler code for now.
//TODO: Write Propert Unit tests/integration tests without mock which can verify ES Repository functionality from installation time.
//Currently ES Repository called ElasticTests is created and deleted but it doesnt get used as the current autowiring of ESRepositoryInterface doesnt work with Unit tests using examples from internet for CrudRepository

//@RunWith(SpringRunner.class)//
//@SpringBootTest(classes = EntityRepository.class)
@RunWith(MockitoJUnitRunner.class)
//@EnableJpaRepositories("ElasticsearchRepository")
//@EnableElasticsearchRepositories(basePackages = "org.askw.simba.autoindex.es.repository")
//@ComponentScan({"org.aksw.simba.autoindex.es.config" ,"org.aksw.simba.autoindex.es.repository" , "org.aksw.simba.autoindex.datasource.sparql"})
public class EntityRepositoryTest {
	
	 // @Autowired
	//  private static ElasticsearchTemplate elasticSearchTemplate;
		
	  private static Client client_;
	  private static Node node_;
	  private static String storagePath_ = "./elasticTests";
	  //@Mock
		//private FileHandler fileHandler = Mockito.mock(FileHandler.class);
	  @Mock
	  private Page<Entity> pageEntityspy = Mockito.mock(PageImpl.class);
		
	  @Mock 
	  private SparqlHandler sparqlHandler = Mockito.mock(SparqlHandler.class);
	  @Mock
	  private ElasticSearchRepositoryInterface elasticSearchRepositoryInterface = Mockito.mock(ElasticSearchRepositoryInterface.class);
	  @InjectMocks
	  private EntityRepository entityRepository = new EntityRepository() ;
	  
	  @Before
	  public void setUp() {
			MockitoAnnotations.initMocks(this);
			//elasticSearchTemplate = new ElasticsearchTemplate(client_);
	  }
		
	@BeforeClass
	public static void createESIndex() throws IOException {
		
		Settings.Builder elasticsearchSettings = Settings.settingsBuilder()
			//      .put("http.enabled", "false")
			      .put("path.home", storagePath_);

	    node_ = new NodeBuilder()
	      .local(true)
	      .settings(elasticsearchSettings.build())
	      .node();
	    client_ = node_.client();  
	    
	}
	
	@AfterClass
	public static void tearDown() throws IOException {
		node_.close();
		deleteStorage();
	}
	
	public static void deleteStorage() throws IOException
	  {
	    File storage = new File(storagePath_);

	    if(storage.exists())
	    {
	      FileUtils.deleteDirectory(storage);
	    }

	  }
	
	public Entity createEntity(String label, String uri) {
		Entity entity = new Entity();
		entity.setLabel(label);
		entity.setUrl(uri);
		return entity;
	}
	
	public Property createProperty(String label, String uri) {
		Property property = new Property(label, uri);
		return property;
	}
	
	public DataClass createClass(String label, String uri) {
		DataClass dataclass = new DataClass(label, uri);
		return dataclass;
	}
	
	@Test
	public void testAddHead() {
		Response response = new Response();
		List<String> contentList = new ArrayList<String>();
		contentList.add("label");
		contentList.add("uri");
		response = entityRepository.addHead(response);
		assertTrue(response.getHead() != null);
		assertTrue(response.getHead().getVars().size() == 2);
		assertTrue(contentList.contains(response.getHead().getVars().get(0)));
		assertTrue(contentList.contains(response.getHead().getVars().get(1)));
	}
	
	@Test
	public void testcreateResponse() {
		List<Entity> entityList = new ArrayList<Entity>();		
		entityList.add(createEntity("Test1" , "test1@test.com"));
		entityList.add(createEntity("Test2" , "test2@test.com"));
		List<String> contentList = new ArrayList<String>();
		contentList.add("label");
		contentList.add("uri");
		Response response = entityRepository.createResponse(entityList);
		assertTrue(response.getHead() != null);
		assertTrue(response.getHead().getVars().size() == 2);
		assertTrue(contentList.contains(response.getHead().getVars().get(0)));
		assertTrue(contentList.contains(response.getHead().getVars().get(1)));
		assertTrue(response.getResults()!= null);
		assertTrue(response.getResults().getBindings() != null);
		assertTrue(response.getResults().getBindings().get(0) != null);
		assertTrue(response.getResults().getBindings().get(0).getLabel()!=null);
		assertTrue(response.getResults().getBindings().get(1).getLabel()!=null);
		assertTrue(response.getResults().getBindings().get(0).getUri()!=null);
		assertTrue(response.getResults().getBindings().get(1).getUri()!=null);
		assertTrue(response.getResults().getBindings().get(0).getLabel().getValue().equals("Test1"));
		assertTrue(response.getResults().getBindings().get(1).getLabel().getValue().equals("Test2"));
		assertTrue(response.getResults().getBindings().get(0).getUri().getValue().equals("test1@test.com"));
		assertTrue(response.getResults().getBindings().get(1).getUri().getValue().equals("test2@test.com"));
	}
	
	@Test
	public void testGetCategory() {
		Category category = Category.CLASS;
		String strCategory = entityRepository.getCategory(category);
		assertTrue("class".equals(strCategory));
		category = Category.PROPERTY;
		strCategory = entityRepository.getCategory(category);
		assertTrue("property".equals(strCategory));
		category = Category.ENTITY;
		strCategory = entityRepository.getCategory(category);
		assertTrue("entity".equals(strCategory));
		category = Category.ALL;
		strCategory = entityRepository.getCategory(category);
		assertTrue("all".equals(strCategory));
	}
	
	@Test
	public void testGetType() {
		Type type = Type.LABEL;
		String strType = entityRepository.getType(type);
		assertTrue("label".equals(strType));
		type = Type.URI;
		strType = entityRepository.getType(type);
		assertTrue("url".equals(strType));
		type = Type.NONE;
		strType = entityRepository.getType(type);
		assertTrue("".equals(strType));
	}
	//Should make this run instead of the mocked version. ES Repository interface is not easy to Integration test functionality from install time.
/*	@Test
	public void testHandleFile() {
		Request request = new Request();
		ArrayList<String> fileList = new ArrayList<String>();
		fileList.add("src/test/resources/dbpedia_3Eng_class.ttl");
		request.setFileList(fileList);
		request.setRequestType("filepath");
		request.setUseLocalDataSource(false);
		ArgumentCaptor<ArrayList<Entity>> argument = ArgumentCaptor.forClass(ArrayList.class);
		Mockito.verify(elasticSearchRepositoryInterface).save(argument.capture());
		Response response = entityRepository.handleFile(request);	
		//System.out.println("Size=" + argument.getValue().size());
		assertTrue(argument.getValue().size() == 655); //Known in advance this file has 655 entries.
		assertTrue(response != null);
		assertTrue(response.getBoolean()== true);
	}*/
	/*@Test
	public void testCreateIndexWithFileHandler() throws IOException {
		//Add Fake entities to index
		ArrayList<Entity> entityList = new ArrayList<Entity>();		
		entityList.add(createEntity("Test1" , "test1@test.com"));
		entityList.add(createEntity("Test2" , "test2@test.com"));
		Request request = new Request();
		request.setRequestType("filePath");
		List<String> fileList = new ArrayList<String>();
		fileList.add("file1.ttl");
		fileList.add("file2.ttl");
		request.setFileList(fileList);
		request.setUseLocalDataSource(false);
		request.setUserId("0000001");
		Mockito.doReturn(entityList).when(fileHandler).indexInputFile(Mockito.anyString());
		Mockito.doReturn(null).when(elasticSearchRepositoryInterface).save(entityList);
		Response response = entityRepository.createIndex(request);
		Mockito.verify(elasticSearchRepositoryInterface , Mockito.times(2)).save(Mockito.any(ArrayList.class)); //One for each file.
		Mockito.verify(fileHandler , Mockito.times(2)).indexInputFile(Mockito.anyString());
		assertTrue(response != null);
		assertTrue(response.getBoolean()== true);	
	}*/
	@Test
	public void testCreateIndexWithEndPointURL() throws IOException {
		//Add Fake entities to index
		ArrayList<Entity> entityList = new ArrayList<Entity>();		
		entityList.add(createEntity("Test1" , "test1@test.com"));
		entityList.add(createEntity("Test2" , "test2@test.com"));
		ArrayList<Property> propertyList = new ArrayList<Property>();
		propertyList.add(createProperty("Property1" , "property1@test.com"));
		propertyList.add(createProperty("Property2" , "property2@test.com"));
		ArrayList<DataClass> classList = new ArrayList<DataClass>();
		classList.add(createClass("Class1" , "class1@test.com"));
		classList.add(createClass("Class2" , "class2@test.com"));
		Request request = new Request();
		request.setRequestType("URI");
		EndPointParameters endPointParameters = request.getEndPointParameters();
		endPointParameters.setUrl("http://dbpedia.org/sparql");
		request.setUseLocalDataSource(false);
		request.setUserId("0000001");
		Mockito.doReturn(entityList).when(sparqlHandler).fetchFromSparqlEndPoint(request);
		Mockito.doReturn(null).when(elasticSearchRepositoryInterface).save(entityList);
		Mockito.doReturn(propertyList).when(sparqlHandler).fetchProperties(request);
		Mockito.doReturn(null).when(elasticSearchRepositoryInterface).save(propertyList);
		Mockito.doReturn(classList).when(sparqlHandler).fetchClasses(request);
		Mockito.doReturn(null).when(elasticSearchRepositoryInterface).save(classList);
		//verify if ES Save is called correct number of times , SParql Handler too.
		//Mockito.verify(sparqlHandler , Mockito.times(1)).fetchFromSparqlEndPoint(request);
		//Mockito.verify(elasticSearchRepositoryInterface , Mockito.times(3)).save(Mockito.any(ArrayList.class));
		Response response = entityRepository.createIndex(request);
		assertTrue(response != null);
		assertTrue(response.getBoolean());
		
		
	}
	@Test
	public void testSearch() {
		SearchRequest searchRequest = new SearchRequest();
		List<Entity> entityList = new ArrayList<Entity>();
		entityList.add(createEntity("Barack Obama" , "http://http://dbpedia.org/page/Barack_Obama"));
		entityList.add(createEntity("Barack" , "http://http://dbpedia.org/page/Barack"));
		searchRequest.setQuery("Obama");
		searchRequest.setCategory("entity");
		searchRequest.setType("label");
		Mockito.doReturn(pageEntityspy).when(elasticSearchRepositoryInterface).search(Mockito.any(SearchQuery.class));
		Mockito.doReturn(entityList).when(pageEntityspy).getContent();
		Response response = entityRepository.search(searchRequest);
		List<String> contentList = new ArrayList<String>();
		contentList.add("label");
		contentList.add("uri");
		assertTrue(response.getHead() != null);
		assertTrue(response.getHead().getVars().size() == 2);
		assertTrue(contentList.contains(response.getHead().getVars().get(0)));
		assertTrue(contentList.contains(response.getHead().getVars().get(1)));
		assertTrue(response.getResults()!= null);
		assertTrue(response.getResults().getBindings() != null);
		assertTrue(response.getResults().getBindings().get(0) != null);
		assertTrue(response.getResults().getBindings().get(0).getLabel()!=null);
		assertTrue(response.getResults().getBindings().get(1).getLabel()!=null);
		assertTrue(response.getResults().getBindings().get(0).getUri()!=null);
		assertTrue(response.getResults().getBindings().get(1).getUri()!=null);
		assertTrue(response.getResults().getBindings().get(0).getLabel().getValue().equals("Barack Obama"));
		assertTrue(response.getResults().getBindings().get(1).getLabel().getValue().equals("Barack"));
		assertTrue(response.getResults().getBindings().get(0).getUri().getValue().equals("http://http://dbpedia.org/page/Barack_Obama"));
		assertTrue(response.getResults().getBindings().get(1).getUri().getValue().equals("http://http://dbpedia.org/page/Barack"));
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEmptyQuerySearch() {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.setQuery("");
		searchRequest.setCategory("entity");
		searchRequest.setType("label");
		entityRepository.search(searchRequest);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEmptyCategorySearch() {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.setQuery("Obama");
		//searchRequest.setCategory("1234"); //Category not set
		searchRequest.setType("label");
		entityRepository.search(searchRequest);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEmptyTypeSearch() {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.setQuery("Obama");
		searchRequest.setCategory("entity"); 
		searchRequest.setType("aaa");
		entityRepository.search(searchRequest);
	}
	//Not implemented yet in source
	@Test
	public void testCreateIndexWithLocalDataSource() throws IOException {
		Request request = new Request();
		EndPointParameters endPointParameters = request.getEndPointParameters();
		endPointParameters.setUrl("http://dbpedia.org/sparql");
		request.setRequestType("URI");
		request.setUseLocalDataSource(true);
		request.setUserId("0000001");
		Response response = entityRepository.createIndex(request);
		assertTrue(response != null);
		assertTrue(response.getBoolean());	
	}
	//Not implemented yet in source
	@Test
	public void testCreateIndexWithLocalDB() throws IOException {
		Request request = new Request();
		request.setRequestType("localdatabase");
		request.setUseLocalDataSource(false);
		request.setUserId("0000001");
		Response response = entityRepository.createIndex(request);
		assertTrue(response != null);
		assertTrue(!response.getBoolean());	
	}
	
	@Test 
	public void testEmptyRequestTypeForIndex() throws IOException {
		Request request = new Request();
		EndPointParameters endPointParameters = request.getEndPointParameters();
		endPointParameters.setUrl("http://dbpedia.org/sparql");
		request.setRequestType("asas"); // 
		request.setUseLocalDataSource(false);
		request.setUserId("0000001");
		Response response = entityRepository.createIndex(request);
		assertTrue(response != null);
		assertTrue(!response.getBoolean());	
	}
	
}
