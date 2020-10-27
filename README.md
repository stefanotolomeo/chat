# Chat Message
An action-monitor application to manage multiple-user message chat.

## Overview
The goal of this project is to provide a real-time chat application.

Use your browser to connect to the application on localhost. 
The welcome page asks you to insert your username and then you can start chatting with other online users.

## Monitoring
Audit records, Messages and Users Log-in/out events are saved into internal REDIS caches.
Thus, there are three different caches:
1. User-Cache: it contains all the users logged into the application;
2. Message-Cache: it contains all the messages sent during the chat;
3. Audit-Cache: it contains all the events on the cache (e.g. user saved, user deleted, message saved, etc.).

Application monitoring could be done in two different way:
1. Use the Swagger on [http://localhost:10091/swagger-ui.html](http://localhost:10091/swagger-ui.html) and query the desired cache.  The audit cache contains the complete story of the chat.
2. Make your own call to the endpoint, for instance using Postman (discouraged, use the swagger instead)

See [https://swagger.io/](https://swagger.io/) for further details.

## Api Documentation

<details>
<summary><b>USER CACHE</b></summary>
 
**Endpoint**: <code>`GET /api/redis/user/all` </code>

Return: all users

**Endpoint**: <code>`GET /api/redis/user/<string:id>` </code>

Return: the requested user
```json
{
    "id": "string", 
    "username": "string"
}
```

**Endpoint**: <code>`DELETE /api/redis/user/<string:id>` </code>

Return: an ack/error message

**Endpoint**: <code>`POST /api/redis/user` </code>

Return: an ack/error message

Request Parameter:
```json
{
    "username": "string"
}
```

**Endpoint**: <code>`PUT /api/redis/user` </code>

Return: an ack/error message

Request Parameter:
```json
{
    "id": "string", 
    "username": "string"
}
```

</details>

<details>
<summary><b>MESSAGE CACHE</b></summary>
 
**Endpoint**: <code>`GET /api/redis/message/all` </code>

Return: all messages

**Endpoint**: <code>`GET /api/redis/message/<string:id>` </code>

Return: the requested message
```json
{
  "content": "string",
  "id": "string",
  "sender": "string",
  "timestamp": "2020-10-27T12:46:14.614Z",
  "topic": "string"
}
```

**Endpoint**: <code>`DELETE /api/redis/message/<string:id>` </code>

Return: an ack/error message

**Endpoint**: <code>`POST /api/redis/message` </code>

Return: an ack/error message

Request Parameter:
```json
{
  "content": "string",
  "sender": "string",
  "topic": "string"
}
```

**Endpoint**: <code>`PUT /api/redis/message` </code>

Return: an ack/error message

Request Parameter:
```json
{
  "content": "string",
  "id": "string",
  "sender": "string",
  "timestamp": "2020-10-27T12:46:14.614Z",
  "topic": "string"
}
```

</details>

<details>
<summary><b>AUDIT CACHE</b></summary>
 
**Endpoint**: <code>`GET /api/status/audit/all` </code>

Return: all messages

**Endpoint**: <code>`GET /api/status/audit/<string:id>` </code>

Return: the requested message
```json
{
  "id": "string",
  "operationType": "INSERT",
  "recordContent": "string",
  "recordId": "string",
  "table": "string",
  "timestamp": "2020-10-27T12:50:54.952Z"
}
```

</details>


## Project Management
This Application is developed in JAVA 8 and Spring Framework (in particular, Spring Boot).

Project and dependencies are managed by MAVEN (https://maven.apache.org/install.html).

### Persistency
Persistency is obtained using [REDIS](https://redis.io/), a fast in-memory key–value database.
- REDIS Server is provided in a docker container (from image "[redis](https://hub.docker.com/_/redis)")
- REDIS Client: Jedis 

### Version Control System
GIT is the version-control system used in this the project.

"Develop" branch is used for developing new features.
"Main" branch is used for releasing tags.

### Other Tools
- Spring Boot:
    - Web: to build Web Service;
    - Websocket: to easily manage Websocket communication;
    - Thymeleaf: a Java template engine;
    - Data-Redis: to easily manage Redis on client side;
- Log4J: to manage different logs;
- Jackson: to marshall and unmarshall;
- Google Guava: to check preconditions;
- Swagger: to provide a GUI for RESTful API;

## Testing
This project contains both Unit and Integration Tests.
 
### Unit Tests
Unit Tests are mainly related to RESTful API controllers test.
It is used Mockito and MockMvc to provide the desired output and then make assertions.

#### Running
Unit-Tests can be run in these different ways:
- Run all Unit-Test: <code> mvn test </code>
- Run a single Unit-Test called "MyTestClass": <code> mvn -Dtest=MyTestClass test </code>
- Run a desired test using your IDE

### Integration Tests
Integration Tests are mainly related to the persistency layer test.

In order to be the most similar as possible to a real scenario, an up and running REDIS docker container is mandatory to succeed. 

#### Preconditions
To run Integration Tests the following tools must be installed:
- Install [Docker](https://docs.docker.com/get-docker/);
- Install [Docker-Compose](https://docs.docker.com/compose/install/);

#### Running
Steps to run integration-test:
1. Turn on docker containers for test:
- Move to folder ./docker: <code>cd docker/ </code>
- Build containers with docker-compose: <code>docker-compose -f docker-compose-it.yml build</code>
- Run containers with docker-compose: <code>docker-compose -f docker-compose-it.yml up</code>

2. Choose the test and run it

3. Stop containers with docker-compose: <code>docker-compose -f docker-compose-it.yml down</code>

## Deploy and Run 
The application can be deploy and run within Docker container using the DockerFile and Docker-Compose. 
1. Install [Docker](https://docs.docker.com/get-docker/);
2. Install [Docker-Compose](https://docs.docker.com/compose/install/);
3. On the project root:
    - Use maven goal to create the jar into /target: <code>mvn clean package</code>
    - Build the container: <code>docker-compose -f docker-compose-chat.yml build </code>
    - Run the container: <code>docker-compose -f docker-compose-chat.yml.yml up</code>
4. Access the application via browser: [http://localhost:10091](http://localhost:10091)
5. Turn off the container: <code>docker-compose -f docker-compose-chat.yml down</code>

## Limitations and Possible Optimizations
1. Move to ActiveMq: configure the Broker, enable STOMP from docker and migrate to ActiveMq. The following dependencies are needed: 
    - Amqp: 
    - ActiveMq:
    - Netty:
2. Manage security and possible related-issues;
3. Improve RESTful API: add "Accept" properties, respond with error status, implement security, version the API
4. Use a dedicated model for DTO;
5. Improve the log pattern for messages into the dedicated log-file.