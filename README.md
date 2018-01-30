# Falcon.io - interview

Data processing pipeline application

## Getting Started

Checkout the project
Install an H2 DB and start it with server mode - [detailed information](http://www.h2database.com/html/installation.html)
* start H2 console
* Setting: Generic H2 (Server)
* Driver: org.h2.Driver
* JDBC URL: jdbc:h2:tcp://localhost/~/interview
* User: "user"
* Password: ""

Install and start Redis [official site](https://redis.io/download) or [unofficial Win64 version](https://github.com/rgl/redis/downloads)
* By default it will start on localhost:6379

Feel free to update / modify default setting, but do not forget to update the application.proerties file as well.

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
![Alt text](websocket_endpoint.PNG?raw=true "websocket")

For sending requests to the Rest endpoints I recommend [Postman](https://www.getpostman.com/) but feel free to use any other message sender application.


## Running the tests

* Run **maven test** goal to run the Unit tests (integration tests are excluded)
* Run **maven integration-test** goal to run integration tests.
* Run **maven verify** goal to run both the integration and Unit tests.
* With **mvn install** both integration and Unit tests will run.

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Spring](https://spring.io/) - Dependency injection framework
* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Data JPA](https://projects.spring.io/spring-data-jpa/) - Used for data access
* [Redis](https://projects.spring.io/spring-data-jpa/) - In memory key-value data structure



