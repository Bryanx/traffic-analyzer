# Traffic Analyzer
## Custom Fine Evaluations
To create a custom fine evaluation follow these steps:
- Create a class that extends _FineEvaluationService_.
- Override "checkForFine(message)" and check if the message should get a fine.
- If it does, create a new Fine with "createFine".

For fines based on a segment:
- Call "getConnectedCameraMessage" to find the corresponding message in the same segment.
- Proceed by checking both messages and optionally calling "createFine".

## Used articles
I've only noted the articles that were most beneficial to the project.
- [GaryGregory: Understanding jUnit method order execution](https://garygregory.wordpress.com/2011/09/25/understaning-junit-method-order-execution/)
- [Baeldung: Spring Cache tutorial](https://www.baeldung.com/spring-cache-tutorial)
- [Baeldung: Guide to Spring Retry](https://www.baeldung.com/spring-retry)
- [Baeldung: Working with fragments in Thymeleaf](https://www.baeldung.com/spring-thymeleaf-fragments)
- [Baeldung: Logging in Spring Boot](https://www.baeldung.com/spring-boot-logging)
- [Baeldung: Intro to the Jackson ObjectMapper](https://www.baeldung.com/jackson-object-mapper-tutorial)
- [Baeldung: Spring Security Form Login](https://www.baeldung.com/spring-security-login)
- Baeldung: Building your API with Spring (e-book)
- [Github: Thymeleaf fragment project](https://github.com/eugenp/tutorials/tree/master/spring-thymeleaf)
- [StackOverflow: How to stop consuming messages with @RabbitListener](https://stackoverflow.com/questions/41035454/how-to-stop-consuming-messages-with-rabbitlistener)
- [Thymeleaf: Tutorial: Thymeleaf + Spring](https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html)
- [Spring.io: Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
- [Spring.io: Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)
- [Spring.io: Spring Data JPA - Reference Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Nakou: Creating a Blog System with Spring MVC, Thymeleaf, JPA and MySQL](http://www.nakov.com/blog/2016/08/05/creating-a-blog-system-with-spring-mvc-thymeleaf-jpa-and-mysql/)
