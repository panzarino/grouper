#!/bin/bash -e

rm -rf target/*
mvn3 install
java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port $PORT target/*.war