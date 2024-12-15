#!/usr/bin/env bash

DOCKER_IMAGE_PREFIX="ivanfranchin"
APP_VERSION="1.0.0"

PRODUCER_APP_NAME="producer-service"
CONSUMER_APP_NAME="consumer-service"
PRODUCER_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${PRODUCER_APP_NAME}:${APP_VERSION}"
CONSUMER_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${CONSUMER_APP_NAME}:${APP_VERSION}"

SKIP_TESTS="true"

if [ "$1" = "native" ];
then
  ./mvnw -Pnative clean spring-boot:build-image --projects "$PRODUCER_APP_NAME" -DskipTests="$SKIP_TESTS" -Dspring-boot.build-image.imageName="$PRODUCER_DOCKER_IMAGE_NAME"
  ./mvnw -Pnative clean spring-boot:build-image --projects "$CONSUMER_APP_NAME" -DskipTests="$SKIP_TESTS" -Dspring-boot.build-image.imageName="$CONSUMER_DOCKER_IMAGE_NAME"
else
  ./mvnw clean spring-boot:build-image --projects "$PRODUCER_APP_NAME" -DskipTests="$SKIP_TESTS" -Dspring-boot.build-image.imageName="$PRODUCER_DOCKER_IMAGE_NAME"
  ./mvnw clean spring-boot:build-image --projects "$CONSUMER_APP_NAME" -DskipTests="$SKIP_TESTS" -Dspring-boot.build-image.imageName="$CONSUMER_DOCKER_IMAGE_NAME"
fi
