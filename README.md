#Checkout the code
```git clone https://github.com/ciany85/message-app.git```

#Run Consul in a Docker container
##First run
```docker run -p 8500:8500 --name=consul consul:1.2.3```
##Run a existing container
```docker start consul```

#Compile the apps
```mvn clean install```
##Compile without activating Consul 
```mvn clean install -Dspring.cloud.consul.enabled=false```
##Run the apps executing the jar file
```java -jar target/message-uuid-generator-0.0.1-SNAPSHOT.jar```

```java -jar target/message-processor-0.0.1-SNAPSHOT.jar```
##Run the app through Spring Boot
```mvn spring-boot:run```

#Test the app
##Consul Dashboard
```http://localhost:8500/ui/dc1/services```
##GET all messages
```curl -X GET http://localhost:8081/message-uuid```
##GET last N messages
```curl -X GET 'http://localhost:8080/message?last=2'```
##ADD messages
```curl -X PUT 'http://localhost:8080/message?message=test1'```

```curl -X PUT 'http://localhost:8080/message?message=test2'```

```curl -X PUT 'http://localhost:8080/message?message=test3'```

##UPDATE a message
```curl -X POST 'http://localhost:8080/message?id=1&message=edited'```

##DELETE a message
```curl -X DELETE 'http://localhost:8080/message?id=2'```

#Docker
## Run Consul
```
docker -d run -p 8500:8500 --name=consul consul:1.2.3
docker start consul
docker stop consul   
```
##Build Message UUID Generator image
```cd message-uuid-generator```

```docker build -t messuuid .```
##Build Message Processor image
```cd message-processor```

```docker build -t messproc .```
##Create and run app containers
```docker run -p 8081:8081 --name=messuuid1 --link consul:consul messuuid```

```docker run -p 8080:8080 --name=messproc --link messuuid:messuuid messproc```
##See container logs
```docker logs -f message-processor```

#Docker Compose
##Init
```docker swarm init```
##Run the stack
```docker stack deploy -c docker-compose.yml message-app```
###Verify running services
```docker service ls```
##Verify App containers
```docker service ps message-app_message-processor```
##Scale a service
```docker service scale message-app_message-processor=3```
##See service logs
```
docker service logs message-app_message-processor -f
docker service logs message-app_message-uuid-generator -f
```
##Remove the stack
```docker stack rm message-app```
##Remove the swarm
```docker swarm leave --force```
##Remove all the containers in a compose
```docker-compose down```
##Remove all the images
```docker rmi $(docker images -q)```


