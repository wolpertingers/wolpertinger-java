#!/bin/bash
docker-compose -f wolpertinger-apache/docker-compose.yml -f wolpertinger-java/docker-compose.yml down -v
echo done