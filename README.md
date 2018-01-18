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

5. Add sparql endpoint in [SparqlEnpointHandler class](https://github.com/dice-group/autoindex/blob/master/src/main/java/org/aksw/simba/autoindex/input/SparqlEndpointHandler.java#L20). Default is `http://dbpedia.org/sparql` .

### Queries
1. Get all the resources :

   `localhost:8080/rest/search/all`

2. Get results for a label search

    `localhost:8080/rest/search/label/{LABEL}`

3. Get results for a keyword search

    `localhost:8080/rest/search/url/{URL}`
    
### Change Index class
Add sparql endpoint in [SparqlEnpointHandler class](https://github.com/dice-group/autoindex/blob/master/src/main/java/org/aksw/simba/autoindex/input/SparqlEndpointHandler.java#L20). Default is `http://dbpedia.org/sparql` .

### Docker
First install docker in your system. For ubuntu you may refer to below link. 
[https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-16-04]

For windows users
[https://docs.docker.com/toolbox/toolbox_install_windows/#step-1-check-your-version]

Move to the parent directory of project and execute the below commands 
1. mvn clean
2. mvn install

Now to build your image, type the below command
```
sudo docker build -f DockerFile -t {Name of your image} .
```
To run your image, type the below command
```
sudo docker run -p {Your port id for example 3030}:8080 -t {Name of your image}
```
Go to the browser and hit `http://localhost:3030/home` to see the output

#### Some useful terminal commands for docker
1. To see created images:`docker image ls`
2. To see running containers:`docker container ps`
3. To see all created containers:`docker container ps -a`
