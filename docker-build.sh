#!/usr/bin/env bash

#if [ "$1" = "native" ];
#then
#  ./mvnw clean spring-boot:build-image --projects producer-service -DskipTests
#  ./mvnw clean spring-boot:build-image --projects consumer-service -DskipTests
#else
  ./mvnw clean compile jib:dockerBuild --projects producer-service
  ./mvnw clean compile jib:dockerBuild --projects consumer-service
#fi