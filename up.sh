#!/bin/bash
docker-compose -f wolpertinger-apache/docker-compose.yml up --build -d
mvn -f wolpertinger-java/pom.xml -U clean package -DskipTests
docker-compose -f wolpertinger-java/docker-compose.yml up --build -d
echo done
