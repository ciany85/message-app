#Set up
<h5>Checkout the code</h5>
```git clone https://github.com/ciany85/message-app.git```

<h5>Run Consul in a Docker container</h5>
It's necessarily to have Consul up and running because the apps will try to connect to it unless specified in the command line with a Spring property
<h5>First run</h5>
```docker run -p 8500:8500 --name=consul consul:1.2.3```
<h5>Start and stop an existing Consul container</h5>
```
docker start consul
docker consul stop
```
<h5>Compile the apps</h5>
```mvn clean install```
<h5>Compile without Consul available</h5> 
```mvn clean install -Dspring.cloud.consul.enabled=false```
<h5>Run the apps executing the jar file<h5>
```
java -jar target/message-uuid-generator-0.0.1-SNAPSHOT.jar
java -jar target/message-processor-0.0.1-SNAPSHOT.jar
```
<h5>Run the app through Spring Boot</h5>
```mvn spring-boot:run```

#Test the app
<h5>Consul Dashboard</h5>
```http://localhost:8500/ui/dc1/services```
<h5>GET all messages</h5>
```curl -X GET http://localhost:8081/message-uuid```
<h5>GET last N messages</h5>
```curl -X GET 'http://localhost:8080/message?last=2'```
<h5>ADD messages</h5>
```
curl -X PUT 'http://localhost:8080/message?message=test1'
curl -X PUT 'http://localhost:8080/message?message=test2'
curl -X PUT 'http://localhost:8080/message?message=test3'
```
<h5>UPDATE a message</h5>
```curl -X POST 'http://localhost:8080/message?id=1&message=edited'```
<h5>DELETE a message</h5>
```curl -X DELETE 'http://localhost:8080/message?id=2'```

#Docker
<h5>Run Consul</h5>
See instructions above. For the very first time you need to create a container, then you can just start and stop it, unless you delete it.
```
docker -d run -p 8500:8500 --name=consul consul:1.2.3
docker start consul
docker stop consul   
```
<h5>Build Message-UUID-Generator app image</h5>
```
cd message-uuid-generator
docker build -t messuuid .
```
<h5>Build Message-Processor app image</h5>
```
cd message-processor
docker build -t messproc .
```
<h5>Create and run app containers</h5>
We need to link the apps together in order to respect the dependencies between them
```
docker run -p 8081:8081 --name=messuuid1 --link consul:consul messuuid
docker run -p 8080:8080 --name=messproc --link messuuid:messuuid messproc
```
<h5>See container logs</h5>
```docker logs -f message-processor```

#Docker Compose
<h5>Init a Cluster</h5>
```docker swarm init```
<h5>Run the stack</h5>
```docker stack deploy -c docker-compose.yml message-app```
<h5>Verify running services</h5>
```docker service ls```
<h5>Verify App containers</h5>
```docker service ps message-app_message-processor```
<h5>Scale a service</h5>
```docker service scale message-app_message-processor=3```
<h5>See service logs</h5>
```
docker service logs message-app_message-processor -f
docker service logs message-app_message-uuid-generator -f
```
<h5>Remove the stack</h5>
```docker stack rm message-app```
<h5>Remove the swarm</h5>
```docker swarm leave --force```
<h5>Remove all the containers in a compose</h5>
```docker-compose down```
<h5>Remove all the images</h5>
```docker rmi $(docker images -q)```


