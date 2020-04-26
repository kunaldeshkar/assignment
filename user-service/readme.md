Open other terminal tab to start user-service

This service will be run in port 8081

cd to /user-service

execute command to build: mvn clean install

execute command to start user-service: mvn spring-boot:run

verify user-service started:  http://localhost:8081/assignment
Expected message response: "Hello from user-service"


--- 
H2 Database
After service started, login to following url to view data:
http://localhost:8081/console/

Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:profiledb
User Name: sa
Password: password