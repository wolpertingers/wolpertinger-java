#!/bin/bash
docker-compose -f wolpertinger-apache/docker-compose.yml up --build -d
docker-compose -f wolpertinger-java/docker-compose.yml up --build -d
docker-compose -f wolpertinger-vue/docker-compose.yml up --build -d
echo done