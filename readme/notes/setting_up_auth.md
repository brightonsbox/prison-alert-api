# Setting up Auth

Two things that we will consider and set up:

1.  Creating a client that can use OAuth2 to authenticate with the HMPPS Auth server and use the token
    to make requests to another service.
2.  Authenticate the service's own API using OAuth2

## 1. Creating a client

* Create a new package `client.prisonapi`
* Create a new class `PrisonApiClient`
* Add an `@Service` annotation
  ```
  Spring uses classpath scanning to detect such annotations and
  if detected attempts to create an instance of the class as a bean
  and register it in the ApplicationContext'
  
  Basically means that we can almost forget about where and how 
  the instance of the class is created, and things consuming it
  can just assume that it will exist too!
  ```
* Create new test class
* Add a new method `public String getAlert(final long bookingId, final long alertId) { return ""; }` 

* Create new test `getAlert` check it runs in ide and do a `./gradlew test`
* Add `testImplementation 'net.javacrumbs.json-unit:json-unit-assertj:2.19.0'` to get `assertThat` working
* `assertThat(new PrisonApiClient().getAlert(1, 2)).isEqualTo("");`
* Use https://api-dev.prison.service.justice.gov.uk/swagger-ui.html to get an idea of contents of getAlert API endpoint
* Can use `/scripts/api/get-alert.sh` too to check contents
* Create a new package within client.prisonapi: `dto`
* Create a new class: `Alert`
* Could add the annotations:
  ```
  @Getter // Lombok: avoids need for explicitly writing getter methods
  @ToString // Lombok: avoids need for toString method
  @EqualsAndHashCode // Lombok: avoid equals and hash code methods
  @NoArgsConstructor // Lombok: avoid need for marshalling constructor
  @AllArgsConstructor // Lombok: avoid need for constructor with all values
  @Builder // Lombok: avoid need for creating a builder class and methods 
  @JsonIgnoreProperties(ignoreUnknown = true) // Won't error if unknown properties are added in the API - avoids additions to the API becoming breaking changes
  ```
* But should have a good feel for what these all are so we'll write them ourselves
  * Add `private Long alertId;`
  * Getter:
    ```
    public Long getAlertId() { return alertId; }
    ```
  * ToString:
    Add `implementation 'com.google.guava:guava:30.0-jre'`
    ```
    @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("alertId", alertId)
                    .toString();
        }
    ```
  * Builder
    ```
    public Alert(final Builder builder) {
        this.alertId = builder.alertId; 
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Long alertId;
        
        private Builder() { }
        
        public Builder alertId(final long alertId) {
            this.alertId = alertId;
            return this;
        }
        
        public Alert build() {
            return new Alert(this);
        }
    }
    ```
### Setting up the `WebClient`

* Create a new namespace `config`
* Create a new class `WebClientConfig`
* Add `@Configuration` annotation
* Add `private final String prisonApiBaseUrl`
* Add constructor
* Add to build.gradle: `implementation 'org.hibernate:hibernate-validator:6.1.6.Final'`
* Add `@URL` annotation to url and `@Validated` to the class
* Add `@Value("${prison.api.base.url}")` annotation
* Try starting the service... cannot resolve the config!
* Add `prison.api.base.url: "http://localhost:8082"` to `application.properties`
* This could also be supplied as an environment variable `PRISON_API_BASE_URL`, in practice 
  for variables that change between environments, this is what is done.
* Make sure we have in build.gradle:
  ```
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-webflux'
	implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
  ```
* Create the WebClient beans:
  ```
      @Bean(name = "authorizedWebClient")
      WebClient authorizedWebClient(final OAuth2AuthorizedClientManager authorizedClientManager) {
  
          var oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
  
          oauth2Client.setDefaultClientRegistrationId("prison-alert-api");
  
          return WebClient.builder()
                  .apply(oauth2Client.oauth2Configuration())
                  .exchangeStrategies(ExchangeStrategies.builder()
                          .codecs(configurer  -> configurer.defaultCodecs().maxInMemorySize(-1))
                          .build())
                  .build();
      }
  
        @Bean(name = "authorizedWebClient")
        WebClient authorizedWebClient(final OAuth2AuthorizedClientManager authorizedClientManager) {
    
            var oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
    
            oauth2Client.setDefaultClientRegistrationId("prison-alert-api");
    
            return WebClient.builder()
                    .apply(oauth2Client.oauth2Configuration())
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(configurer  -> configurer.defaultCodecs().maxInMemorySize(-1))
                            .build())
                    .build();
        }
  ```

* But still need to add config... in `application.properties`
```
  security:
    oauth2:
      client:
        registration:
          prison-alert-api:
            provider: hmpps-auth
            client-id: ${prison.alert.api.client.id}
            client-secret: ${prison.alert.api.client.secret}
            authorization-grant-type: client_credentials
        provider:
          hmpps-auth:
            token-uri: ${hmpps.auth.base.url}/oauth/token
```

* We'll need to provide these variables... maybe split out into
  an application-dev.properties file with:
  
  ```
  hmpps.auth.base.url: http://localhost:8081/auth
  prison.api.base.url: http://localhost:8082
  prison.alert.api.client.id: omicadmin
  prison.alert.api.client.secret: clientsecret
  ```
* In intellij to run we'll now need: `-Dspring.profiles.active=dev`
* Or can run in cmd line with `./gradlew bootRun --args='--spring.profiles.active=dev'`
* Now pull the WebClient through to our PrisonApiClient
* Add constructor: `public PrisonApiClient(@Qualifier("authorizedWebClient") final WebClient webClient)`
* Can now start to use it!
  ```
  final var uri = prisonAlertApiProperties.getPrisonApiBaseUrl() + format("/api/bookings/%s/alerts/%s", bookingId, alertId)
  return webClient.get()
    .uri(uri)
    .retrieve()
    .bodyToMono(Alert.class)
    .block();
  ```