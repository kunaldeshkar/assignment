Install and start kafka server
Install Kafka and create topic name `profile`

Follow the instruction below from step 1 to step 5:
https://kafka.apache.org/quickstart

Start zookeeper server:
bin/zookeeper-server-start.sh config/zookeeper.properties

Start kafka server:
bin/kafka-server-start.sh config/server.properties

--- 

This service will be run in port 9090

Start auth-service
cd to /auth-service

execute command to build: mvn clean install

execute command to start auth-service: mvn spring-boot:run

verify auth-service started:  http://localhost:9090/assignment
Expected message response: "Hello from auth-service"

--- 
H2 Database
After service started, login to following url to view data:
http://localhost:9090/console/

Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:userdb
User Name: sa
Password: password