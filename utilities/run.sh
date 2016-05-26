#!/bin/bash -e

git pull
mysql-ctl start
rm -rf target/*
mvn3 install
java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port $PORT target/*.war
