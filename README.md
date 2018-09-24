# Set up
**Checkout the code**

`git clone https://github.com/danilocugia/message-app.git`

**Run Consul in a Docker container**

It's necessarily to have Consul up and running because the apps will try to connect to it unless specified in the command line with a Spring property

**First run**

`docker run -p 8500:8500 --name=consul consul:1.2.3`

**Start and stop an existing Consul container**

```
docker start consul
docker consul stop
```

**Compile the apps**

`mvn clean install`

**Compile without Consul available**
 
`mvn clean install -Dspring.cloud.consul.enabled=false`

**Run the apps executing the jar file**

```
java -jar target/message-uuid-generator-0.0.1-SNAPSHOT.jar
java -jar target/message-processor-0.0.1-SNAPSHOT.jar
```

**Run the app through Spring Boot**

`mvn spring-boot:run`

# Test the app
**Consul Dashboard**

`http://localhost:8500/ui/dc1/services`

**GET all messages**

`curl -X GET http://localhost:8081/message-uuid`

**GET last N messages**

`curl -X GET 'http://localhost:8080/message?last=2'`

**ADD messages**
```
curl -X PUT 'http://localhost:8080/message?message=test1'
curl -X PUT 'http://localhost:8080/message?message=test2'
curl -X PUT 'http://localhost:8080/message?message=test3'
```

**UPDATE a message**

`curl -X POST 'http://localhost:8080/message?id=1&message=edited'`

**DELETE a message**

`curl -X DELETE 'http://localhost:8080/message?id=2'`

# Docker
**Run Consul**

See instructions above. For the very first time you need to create a container, then you can just start and stop it, unless you delete it.

```
docker -d run -p 8500:8500 --name=consul consul:1.2.3
docker start consul
docker stop consul
```

**Build *Message UUID Generator* app image**

```
cd message-uuid-generator
docker build -t messuuid .
```

**Build *Message Processor* app image**

```
cd message-processor
docker build -t messproc .
```

**Build apps' images together through Docker Compose**

`docker-compose build`

**Create and run app containers**

We need to link the apps together in order to respect the dependencies between them

```
docker run -p 8081:8081 --name=messuuid1 --link consul:consul messuuid
docker run -p 8080:8080 --name=messproc --link messuuid:messuuid messproc
```

**See container logs**

`docker logs -f message-processor`

# Docker Compose
**Init a Cluster**

`docker swarm init`

**Run the stack**

`docker stack deploy -c docker-compose.yml message-app`

**Verify running services**

`docker service ls`

**Verify App containers**

`docker service ps message-app_message-processor`

**Scale a service**

`docker service scale message-app_message-processor=3`

**See service logs**

```
docker service logs message-app_message-processor -f
docker service logs message-app_message-uuid-generator -f
```

**Remove the stack**

`docker stack rm message-app`

**Remove the swarm**

`docker swarm leave --force`

**Remove all the containers in a compose**

`docker-compose down`

**Remove all the images**

`docker rmi $(docker images -q)`
