# Falcon.io - interview

Data processing pipeline application

## Getting Started

* Checkout the project
* **Without Docker:**
  * Install MySql server and start it - [MySql download](https://dev.mysql.com/downloads/windows/installer/5.7.html)
      * Create DB:  interview
      * User:     interview
      * Password: interview
 
  * Install and start Redis [official site](https://redis.io/download) or [unofficial Win64 version](https://github.com/rgl/redis/downloads)
    * By default it will start on localhost:6379
  
  * Start the application with "-Dspring.profiles.active=dev" VM parameter.

* **With Docker:**
  * Execute a docker-compose file with the following content:
```
version: '2'
services:
  redis-server:
    image: redis
    ports:
     - "6379:6379"
  mysql-server:
    image: mysql
    environment:
       MYSQL_ROOT_PASSWORD: root 
       MYSQL_DATABASE: interview 
       MYSQL_USER: interview 
       MYSQL_PASSWORD: interview 
       MYSQL_ROOT_HOST: \%
    ports:
     - "3306:3306"
```
  * Start the application with "-Dspring.profiles.active=dev" VM parameter.
  * The application will initialize a new table on start.




Feel free to update / modify default setting or use any other DB server, but do not forget to update the application.proerties file as well.

## Usage

After the server has been started, you are able to call in to the following Rest endpoints:

* [host:port/get](localhost:8080/get) - GET endpoint to get all the previously persisted messages.
```
GET /get HTTP/1.1
Host: localhost:8080
```

Response:
```
[
    {
        "id": 1,
        "messageText": "This is a test message."
    }
]
```
* [host:port/add](localhost:8080/add) - POST endpoint to send a new message text to the server (just put the message text to the body without brackets).
```
POST /add HTTP/1.1
Host: localhost:8080
Content-Type: application/json

This is a test message.
```
* [index](localhost:8080/) - Simple HTML page to show the real time message delivery

## Running the tests

* Run **maven test** goal to run the Unit tests (integration tests are excluded)
* Run **maven integration-test** goal to run integration tests.
* Run **maven verify** goal to run both the integration and Unit tests.
* With **mvn install** both integration and Unit tests will run, and a Docker image will be created

## Deployment

* If you have made any changes in the code, run mvn clean install. The docker-maven-plugin will create a new Docker image, called "interview".
* Create a docker-compose file (docker-compose.yml) and paste the following content:
```
version: '2'
services:
  redis-server:
    image: redis
  mysql-server:
    image: mysql
    environment:
       MYSQL_ROOT_PASSWORD: root 
       MYSQL_DATABASE: interview 
       MYSQL_USER: interview 
       MYSQL_PASSWORD: interview 
       MYSQL_ROOT_HOST: \%
  interview-server:
    image: interview
    depends_on:
     - redis-server
     - mysql-server
    links:
     - redis-server
     - mysql-server
    ports:
     - "8080:8080"
```
* Open cmd or bash next to the docker-compose file and enter the following command:
```
docker-compose up
```
* It will start the docker container with an open port on **localhost:8080**
* If you haven't made any changes then create and run a docker-compose file with the following content 
(or use the existing one from the root of this repository):
```
version: '2'
services:
  redis-server:
    image: redis
  mysql-server:
    image: mysql
    environment:
       MYSQL_ROOT_PASSWORD: root 
       MYSQL_DATABASE: interview 
       MYSQL_USER: interview 
       MYSQL_PASSWORD: interview 
       MYSQL_ROOT_HOST: \%
  interview-server:
    image: danielkollanyi/interview
    depends_on:
     - redis-server
     - mysql-server
    links:
     - redis-server
     - mysql-server
    ports:
     - "8080:8080"
```
* It will pull down the docker image from a public docker repository

## Built With

* [Spring](https://spring.io/) - Dependency injection framework
* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Data JPA](https://projects.spring.io/spring-data-jpa/) - Used for data access
* [Redis](https://projects.spring.io/spring-data-jpa/) - In memory key-value data structure



