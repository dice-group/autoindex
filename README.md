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

4. Use Postman or Curl command to access the rest api 
  
### Queries
1. Get all the resources :

   `localhost:8080/rest/search/all`

2. Get results for a label search

    `localhost:8080/rest/search/label/{LABEL}`

3. Get results for a keyword search

    `localhost:8080/rest/search/url/{URL}`
    
