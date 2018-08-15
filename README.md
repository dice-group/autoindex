# Autoindex
[![Build Status](https://travis-ci.org/dice-group/autoindex.svg?branch=master)](https://travis-ci.org/dice-group/autoindex)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5da35f0710b845ba968aa98863556d7a)](https://www.codacy.com/app/idreestahir/autoindex?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dice-group/autoindex&amp;utm_campaign=Badge_Grade)
[![BCH compliance](https://bettercodehub.com/edge/badge/dice-group/autoindex?branch=master)](https://bettercodehub.com/)

Autoindex are a set of REST API's that aims to index various types of RDF data and also query it. It is currently a work in progress. A UI is also available for test under http://localhost:9091/.

Please go to the end of this ReadMe file for a simple demo of the UI.

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
Parameters  : endPointParameters : {"url":Valid End Point URL} , requestType : URI , userId : {if any} , useLocalDataSource : false (true to use a local Extraction data source) , default_graph : { if any}

If there are Select Queries to be modified or to know what default Select Queries would be executed, please refer below to Section "Customizing Select Queries".

Create Index from File : Require Multipart Form data , API : /index/uploadFile
Parameters: userId {if any}

CreateIndex From any Local Sparql End point: /index/create

Parameters  : endPointParameters : {"url":Valid LocalHost End Point URL} , useLocalDataSource : true

The Query passed would be executed as is on the Local End point URL. If you would prefer the Query present in application.properties to be executed, isEntityCustomized and entitySelectQuery can be ignored and not passed as parameter. Similar to Remote End Points, Local Sparql End points also support Query Customization and indexing of entity, classes and property ( either by default queries present under application.properties or by user provided custom queries). For custom queries, please refer to the section under "Customizing Select Queries". 

Add any single Custom key,value Pair:
Parameters: requestType : custom, userId : {if any} , useLocalDataSource : false (true to use a local Extraction data source), a json field "keys" which takes {"firstKey: key , "secondKey": value , "category": (Entity,Property or Class) } 

Search : /search
Parameters: query : { Query to search} , type: {LABEL or URI}, userId : {if any and must match the id provided during createIndex} , category : {Either Entity , Class or Property}. 
For custom options which are supported by Search, please refer to section below under Section "Search Options".


### Customizing Select Queries
application.properties file contains options to edit/add new prefixes , modify existing select queries for entity,class and property by modifying entity.whereclause , class.whereclause and property.whereclause fields respectively.

To add new prefixes, go to the prefix list, add a new entry, both name and url are mandatory. If the last line in prefixes in configuration file is prefix4.url then you can add a new entry as prefix5.name and prefix5.url and so on.

Additionally, Select Queries can be customized through the Rest interface call of Indexing any EndPoint URL.

Create Index : /index/create 
Parameters  : endPointParameters : {"url":Valid End Point URL , "isEntityCustomized": true , "isPropertyCustomized" : true , "isClassCustomized" : true , "entitySelectQuery" : Select Query for indexing Entities with necessary Prefixes , "propertySelectQuery" :Select Query for indexing Properties with necessary Prefixes , "classSelectQuery" : Select Query for indexing classes with necessary Prefixes} , requestType : URI , userId : {if any} , useLocalDataSource : false (true to use a local Extraction data source) , default_graph : { if any}.

Please note the following when there is a need to customize select Queries through Rest interfaces.
1. isEntityCustomized , isPropertyCustomized or isClassCustomized can be true when the user wants to overwrite the default Select Query with the select Query provided in the rest Interface. Please note it is mandatory to provide a value for entitySelectQuery when isEntityCustomized is passed as true. Similarly for properties and class Select Queries. By default, isEntityCustomized , isPropertyCustomized and isClassCustomized are optional and is assigned as false even if parameter is not passed.

2. Select Query for indexing Properties with necessary Prefixes : Select Queries provided should follow Sparql standards. Select Query is one complete text field which can also contain prefixes.
Prefixes should be of the form PREFIX key:<value>, For example : PREFIX owl: <http://www.w3.org/2002/07/owl#>. Please note that when customizing Select Queries, only the values provided in the rest interface calls as a part of Select Query will be considered and the default prefixes will not be considered. 
    
### Search Options

Support for Search Options would be similar to what is supported by Lucene and Elastic Search. These include

1. Regular URL and Label Search.
2. Proximity Search with ~, For example "AAA"~5.
3. Regular expressions with string contained between / and /. For example /A.B/
4. Wild card search with * and ?.
5. Range Search For example [Aa TO Bb] and {Aa TO Bb}.
6. Boolean Search (OR AND NOT + and -).
7. Fuzzy search with similarity weight For example AA~0.8
8. Term Boosting. For example "Michelle"^4 "Obama"
9. Term Grouping with Boolean Search.

### Connecting to Eureka Server

If there is a Eureka Server or other discovery client with which you want Auto Index to connect to, modify application.yml file as below:
Change eureka.client.enabled to true and set serviceurl.defaultzone to the url of the eureka server to connect to. By default, AutoIndex has Discovery set to false and will not connect to any discovery client.

Please note Eureka Server must be running when eureka.client.enabled=true. Otherwise the application will not start and there would be a lot of errors from DiscoveryClient.

By connecting to eureka server, Auto Index behaves as a microservice and components can send Rest calls using http://AUTOINDEX.
    
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
sudo docker build -f Dockerfile -t autoindex_local .
```
To run your image, type the below command.
```
sudo docker run -d -p 8186:9091 -t autoindex_local --restart always
```
To pull image from the hub,type the below command in terminal.
```
sudo docker run -d -p 8186:9091 dice-group/autoindex --restart always
```

#### Some useful terminal commands for docker
1. To see created images:`docker image ls`
2. To see running containers:`docker container ps`
3. To see all created containers:`docker container ps -a`

### Using the UI of Auto Index

Indexing RDF Data:

![Indexing RDF Data](https://user-images.githubusercontent.com/33149420/42691297-b7cab9b0-86a7-11e8-9c6f-cc62eca3bc18.gif)

Search Options:

![Search Options](https://user-images.githubusercontent.com/33149420/42704693-250a23a2-86d1-11e8-8e15-ffb43c590218.gif)

