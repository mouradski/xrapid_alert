# Getting started
![Contributions welcome](https://img.shields.io/badge/contributions-welcome-orange.svg)


* Fork this project
* Update the `resources/application.yaml` file to customize persistence on database.

```yaml
spring:
  jpa:
    hibernate.ddl-auto: update
    database-platform: org.hibernate.dialect.Dialect.MySQLInnoDBDialect

  datasource:
    url: jdbc:mysql://localhost:3306/xrapid?autoReconnect=true&useSSL=false
    driverClassName: com.mysql.jdbc.Driver
    username: USERNAME
    password: PASSWORD
```

# Build jar

```
$ cd xrp_alert
$ mvn clean install
```

# Run with maven & spring-boot

```
$ cd xrp_alert
$ mvn spring-boot:run
```

# Test 

go to http://localhost 
