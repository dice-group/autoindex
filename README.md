# Autoindex
Autoindex is a REST API that aims to index any RDF Dump or Sparql Endpoint and the query it. it is currently a work in progress.

## Getting Started

### Prerequisites

Maven is the only pre-requisite for  . The instructions to install maven can be found [here](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html).

### Installing

1. Once the maven is up and running. Clone the repository using the following command in a desired folder.
2. Navigate to the base folder of the project and run the following command
    
    `mvn clean install` 

3. Run the jar file as follows 

   `java -jar target/autoindex-0.0.1-SNAPSHOT.jar`

4. The UI should be available at 

    `localhost:8080/home`

### Change Index class
Add sparql endpoint in [SparqlEnpointHandler class](https://github.com/dice-group/autoindex/blob/master/src/main/java/org/aksw/simba/autoindex/input/SparqlEndpointHandler.java#L20). Default is `http://dbpedia.org/sparql` .

### Queries
Navigate to the query section and fire label queries. URI queries are facing the issue

    
