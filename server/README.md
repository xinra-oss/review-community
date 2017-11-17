Running the server:

1) Set up a database (optional). Currently supported are MySql and PostgreSQL. You can also use an in-memory database.
2) Create a file `server/src/main/resources/application-dev.properties`. For documentation on how to configure the server see [application.properties](https://github.com/xinra-it/review-community/blob/master/server/src/main/resources/application.properties) in the same directory. Minimum configuration that uses the in-memory database:
```
spring.jpa.database=HSQL
spring.jpa.hibernate.ddl-auto=create
```
3) Run the command `./gradlew :server:bootRun` from the root directory of this project. The server listens on port 8080.

When the server is run in the `dev` environment (default) it generates some [sample content](https://github.com/xinra-it/review-community/wiki/Sample-Content).

To run all tests and linting, execute `./gradlew :server:check`.