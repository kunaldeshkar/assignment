# assignment
Spring Boot Microservices Application

# auth-service:

1. Install and start kafka server Install Kafka and create topic name profile
2. Follow the instruction below from step 1 to step 5: https://kafka.apache.org/quickstart
3. Start zookeeper server: bin/zookeeper-server-start.sh config/zookeeper.properties
4. Start kafka server: bin/kafka-server-start.sh config/server.properties
5. This service will run on port 9090
6. Start auth-service cd to /auth-service
7. execute command to build: mvn clean install
8. execute command to start auth-service: mvn spring-boot:run
9. verify auth-service started: http://localhost:9090/assignment Expected message response: "Hello from auth-service"
10. H2 Database After service started, login to following url to view data: http://localhost:9090/console/
11. Driver Class: org.h2.Driver JDBC URL: jdbc:h2:mem:userdb User Name: sa Password: password


# user-service:

1. Open other terminal tab to start user-service
2. This service will be run in port 8081
3. cd to /user-service
4. execute command to build: mvn clean install
5. execute command to start user-service: mvn spring-boot:run
6. verify user-service started: http://localhost:8081/assignment Expected message response: "Hello from user-service"
7. H2 Database After service started, login to following url to view data: http://localhost:8081/console/
8. Driver Class: org.h2.Driver JDBC URL: jdbc:h2:mem:profiledb User Name: sa Password: password
