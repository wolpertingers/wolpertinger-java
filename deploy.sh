#!/bin/bash
mvn -f wolpertinger-java/pom.xml -U clean package -DskipTests
npm install -f wolpertinger-vue/app --prefix wolpertinger-vue/app
npm run build --prefix wolpertinger-vue/app --production
./up.sh