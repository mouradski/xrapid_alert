# Getting started

* Fork this project
* Update the `resources/application.yaml` file to customize persistence on database.

```yaml
spring:
  jpa:
    hibernate.ddl-auto: update
    database-platform: jdbc:mysql://localhost:3306/xrapid?autoReconnect=true&useSSL=false

  datasource:
    url: jdbc:h2:mem:testdb
    driverClassName: com.mysql.jdbc.Driver
    username: USERNAME
    password: PASSWORD
```

# Install

```
$ cd xrp_alert
$ mvn clean install
```

# Deploy with maven & spring-boot

```
$ cd xrp_alert
$ mvn spring-boot:run
```

# Test 

go to http://localhost 
