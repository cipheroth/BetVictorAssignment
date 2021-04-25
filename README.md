# BetVictorAssignment
Hello !

### Running the app

In order to execute this program succesfully you need to have installed

* Maven 3.5 or superior
* Java JDK 11

After cloning the project, entering to the directory execute:

```bash
mvn spring-boot:run
```

## Simple Black Board Architecture of the Solution
![image](https://github.com/cipheroth/BetVictorAssignment/blob/master/ArchitectureBlackBoard.png)

### Using the app

To open a chat window you can enter in a browser to:

```bash
http://localhost:8080
```
You have first to enter your nickname and the nickname of the user you want to chat, then press **connect** to start the websocket connection.
Open many browser pages as you want.
![image](https://github.com/cipheroth/BetVictorAssignment/blob/master/ChatActiveMonitor.jpg)


To open the action monitor enter to:

```bash
http://localhost:8080/action-monitor.html
```

## Rest Services

To see the current version of the app:
```bash
http://localhost:8080/version
```

The status of the current instance of the application is just using **spring actuator**
For example to see the health of the app:
```bash
http://localhost:8080/actuator/health
http://localhost:8080/actuator/health/custom
```
More about spring actuator:
https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html

## To access to H2 database

You can enter to:
```bash
http://localhost:8080/h2-console/
```
Credentials:
```bash
User: sa
Password: password
JDBC URL: jdbc:h2:file:./src/main/resources/testdb;DB_CLOSE_ON_EXIT=FALSE;IFEXISTS=FALSE;DB_CLOSE_DELAY=-1;
```
the only table available is ***MESSAGE_ENTITY***

![image](https://github.com/cipheroth/BetVictorAssignment/blob/master/databaseH2console.jpg)

### About Testing 
About testing for time reasons I have provided only integration testing end to end using *SpringBootTest*
