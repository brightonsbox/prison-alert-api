# prison-alert-api

Initial resource for creating an example Java, Spring Boot application.

## The (mock) scenario:

An application is needed to view a dashboard of new alerts that have 
been added to prisoners for a particular prison. The aim of this 
new service is to listen to alert events via SQS, retrieve data from
the Prison API, persist what is needed for display and expose the data
via an API that a frontend could use for the dashboard.

## The focus 

The focus will be to get familiarity with the following tech:

 * Java
 * Spring Boot (with RESTful API)
 * Spring JMS
 * Spring Data JPA
 * Flyway
 * Maybe Swagger if we get time
