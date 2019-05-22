#!/bin/bash
rm -rf wolpertinger-vue/app/dist wolpertinger-vue/app/node_modules
mvn -f wolpertinger-java/pom.xml clean -DskipTests
./down.sh