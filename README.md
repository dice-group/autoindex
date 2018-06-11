# Autoindex
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5da35f0710b845ba968aa98863556d7a)](https://www.codacy.com/app/idreestahir/autoindex?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dice-group/autoindex&amp;utm_campaign=Badge_Grade)
[![BCH compliance](https://bettercodehub.com/edge/badge/dice-group/autoindex?branch=master)](https://bettercodehub.com/)

Autoindex is a REST API that aims to index any RDF Dump or Sparql Endpoint and the query it. It is currently a work in progress. A UI is also available for test under http://localhost:9091/

## Getting Started

### Prerequisites

Maven is the only pre-requisite. The instructions to install maven can be found [here](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html).

### Installing

1. Once the maven is up and running. Clone the repository using the following command in a desired folder.

    `git clone https://github.com/dice-group/autoindex.git`

2. Navigate to the base folder of the project and run the following command
    
    `mvn clean package` 

3. Run `mvn clean spring-boot:run`

4. Use test Website available under localhost:9091.

For docker, refer to the end of this file.

### Functionalities

1. Index a Endpoint/File/Single Key value pair

    http://localhost:9091/index.html

2. Get results for a label search or URI Search

   http://localhost:9091/query.html

### REST Interfaces available for POST Requests. 

Create Index : /index/create 
Parameters  : url : {Valid End Point URL} , requestType : URI , userId : {if any} , useLocalDataSource : false (true to use a local Extraction data source) , default_graph : { if any}

Create Index from File : Require Multipart Form data , API : /index/uploadFile
Parameters: userId {if any}

Add any single Custom key,value Pair:
Parameters: requestType : custom, userId : {if any} , useLocalDataSource : false (true to use a local Extraction data source), a json field "keys" which takes {"firstKey: key , "secondKey": value , "category": (Entity,Property or Class) } 

Search : /search
Parameters: query : { Query to search} , type: {LABEL or URI}, userId : {if any and must match the id provided during createIndex} , category : {Either Entity , Class or Property}. Wildcards * and ? are supported in the query field.

### Customizing Select Queries
application.properties file contains options to edit/add new prefixes , modify existing select queries for entity,class and property by modifying entity.whereclause , class.whereclause and property.whereclause fields respectively.

To add new prefixes, go to the prefix list, add a new entry, both name and url are mandatory. If the last line in prefixes in configuration file is prefix4.url then you can add a new entry as prefix5.name and prefix5.url and so on.

    
### Docker
First install docker in your system. For ubuntu you may refer to below link. 
[https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-16-04]

For windows users
[https://docs.docker.com/toolbox/toolbox_install_windows/#step-1-check-your-version]

Move to the parent directory of project and execute the below commands 
1. mvn clean
2. mvn install

Now to build your image, type the below command.
```
sudo docker build -f DockerFile -t {Name of your image} .
```
To run your image, type the below command.
```
sudo docker run -p {Your port id for example 3030}:8080 -t {Name of your image}
```
To pull image from the hub,type the below command in terminal.
```
sudo docker run dicegroup/autoindex
```

#### Some useful terminal commands for docker
1. To see created images:`docker image ls`
2. To see running containers:`docker container ps`
3. To see all created containers:`docker container ps -a`
