# pulse-java

Pulse (java version)

## Documentation
Javadoc can be found here: https://liljekvist.github.io/pulse-java/javadoc/
Swagger can be enabled in application.config in a development enviroment.

## Code standard

https://google.github.io/styleguide/javaguide.html

## Build instructions

### Prebuilt war
In releases I have uploaded the latest compiled version of the application. You may skip this step and download it instead.
If any problems occur I suggest building it yourself.

### mvn standalone
Go to ./pulse-java and use to following command
```
.\mvnw.cmd clean package
```
The compilade war will now be in /target

### Intellij
Oppen the folder as a project and configure a build configuration. Set it as maven and use clean package as the parameters.

## Before first startup

### Requirements
* Java 20 (should be in path)
* Maven (should be in path)
* Mariadb with a login (other database vendors may work but are not tested due to hibernate creating the schema automatically.)

#### Database setup
There is a docker compose file in the repository that will setup a mariadb database. You may use it at your discretion. 
Just remember that you need to create the database before running the application. This is an example how to do that.
```
DROP DATABASE IF EXISTS pulse;
CREATE DATABASE pulse;
```


### Confirgure application.config
In the application.config the following information must be changed: Email account, database och quartz parameter. I have pasted a example config below, please use it. Some fields are obligatory. Remember to uncomment # spring.quartz.jdbc.initialize-schema=always before first launch and then follow the instructions in the config file.
```
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.formate_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

spring.datasource.url=jdbc:mariadb://localhost:3306/pulse?useSSL=fals&serverTimezone=UTC&useLegacyDatetimecode=false
spring.datasource.username=pulse
spring.datasource.password=xxx

springdoc.api-docs.enabled=false
springdoc.api-docs.path=/swagger-ui/api/docs
springdoc.swagger-ui.enabled=true

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=projectpulse1337@gmail.com
spring.mail.password=xxx
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

spring.mvc.format.date-time=iso

spring.quartz.job-store-type=jdbc

# On first launch use the following to create the tables
# spring.quartz.jdbc.initialize-schema=always

# On subsequent launches use the following to validate the tables and not overwrite them
# spring.quartz.jdbc.initialize-schema=embedded

spring.quartz.scheduler-name=ReportScheduler
spring.quartz.overwrite-existing-jobs=false
spring.quartz.properties.org.quartz.threadPool.threadCount=10
spring.quartz.properties.org.quartz.jobStore.isClustered=false
```

### File structure
Drag config and war file to a directory. The direcotry should look like this:
![image](https://github.com/liljekvist/pulse-java/assets/38380471/eb483728-8437-4c87-ac65-e3cc25a11729)


## Running the application
To run the application use the java command like this
```
java -jar .\pulse-0.0.1-SNAPSHOT.war
```

## For questions
You can reach me at mibh22@student.bth.se


