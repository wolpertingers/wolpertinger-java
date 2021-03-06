# <img src="logo.png" width="100"/> Wolpertinger JavaEE backend

[![CircleCI](https://circleci.com/gh/wolpertingers/wolpertinger-java.svg?style=svg)](https://circleci.com/gh/wolpertingers/wolpertinger-java)

## Project setup
The project is built with maven and deployed in docker containers.

### Build the .war
```
mvn -U clean package -DskipTests
```

### Start docker containers
```
docker-compose up --build -d
```

## Rest-API access
OpenAPI documentation: `{deployUrl}/openapi`

### Image service
GET all: `{deployUrl}/wolpertinger-java/rest/images`

GET by name: `{deployUrl}/wolpertinger-java/rest/images?name={imageName}`

### Order service
GET all: `{deployUrl}/wolpertinger-java/rest/orders`

GET by id: `{deployUrl}/wolpertinger-java/rest/orders/{id}`

POST to create: `{deployUrl}/wolpertinger-java/rest/orders`

### Token service
POST to create: (protected) `{deployUrl}/wolpertinger-java/rest/tokens` (no data body)

GET all: (protected) `{deployUrl}/wolpertinger-java/rest/tokens`

GET by value: `{deployUrl}/wolpertinger-java/rest/tokens/{value}`
