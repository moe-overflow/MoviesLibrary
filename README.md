# MoviesLibrary

A simple web application for managing movies via http requests.

# Prerequisites

- Java 17
- Maven
- MongoDB 
- Docker Compose

# Usage

Since the application uses the mongoDB database, which runs in a standalone docker container, building
the application purely from the provided Dockerfile is not the proper way.
Instead, it should be built and run using Docker Compose.
Before running application, make sure that a mongoDB service is running on 'localhost:27017'
For this, use these commands:

```
cd <path-to-project>
docker-compose build
docker-compose up
```

After that, the application runs and is available under: http://localhost:8080/movies

Use this for shutting down the application:
```
docker-compose down
```