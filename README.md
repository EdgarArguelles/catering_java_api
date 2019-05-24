CATERING
===============

Web Application to help clients to create their own Menu and provide a price for it.

CATERING is a decoupled web application, it's built by independent backends and frontends
written with different languages working together.

Look the CATERING Repository [here](https://github.com/EdgarArguelles/Catering)

# CATERING Java API
This project is a Java REST / GraphQL API used by CATERING

This web app uses MySQL as Data Base, but its ready to use MongoDB instead

# Live API:
[Demo page](https://cateringjavaapi.herokuapp.com)

# Stack:
- Gradle
- Spring Boot
- Spring Data
- Spring Security
- GraphQL SPQR (GraphQL Schema Publisher & Query Resolver: @Graph annotations)
- OAuth (access with Facebook and Google)
- JJWT (Java JSON Web Token)
- WebSocket / Stomp
- Project Lombok (Getter and Setter Annotations)
- Swagger (REST Documentation)
- Junit
- Jacoco (Junit Code Coverage)

# Changes
All changes must be done on dev branch, because all changes in master will deploy version.

# Switch MySQL by Mongo
Change all Repositories: extends MongoRepository instead of MySQLRepository, for example.

- Change "MenuRepository extends MySQLMenuRepository" for "MenuRepository extends MongoMenuRepository"

# Build (create both jar and war)
    ./gradlew build
- The WAR can be deployed with any application server like Tomcat, JBoos, etc.
- The JAR can be run with Tomcat embedded server with the command: java -jar -Dspring.profiles.active=sql-local catering_java_api-x.x.x.jar

# Run
    ./gradlew bootRun

# Run Tests
    ./gradlew test jacocoTestReport
- Generated report (build/reports/tests/test/index.html)
- Coverage (build/reports/jacoco/test/html/index.html)

# Java Documentation
    ./gradlew javadoc
- Generated report (build/docs/javadoc/index.html)

# REST Documentation
    http://localhost:8000/catering/swagger-ui.html