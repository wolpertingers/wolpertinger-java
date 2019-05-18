#!/bin/bash
docker-compose -f wolpertinger-vue/docker-compose.yml down -v
docker-compose -f wolpertinger-java/docker-compose.yml down -v
docker-compose -f wolpertinger-apache/docker-compose.yml down -v
echo done