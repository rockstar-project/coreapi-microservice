#!/bin/bash

mvn clean package -Dmaven.test.skip=true

docker build -t rockstar/coreapi-microservice .
docker tag rockstar/coreapi-microservice:latest $DOCKER_REGISTRY_URL/rockstar/coreapi-microservice:0.0.1
docker login -u $DOCKER_USER -p $DOCKER_PASSWORD https://$DOCKER_REGISTRY_URL
docker push $DOCKER_REGISTRY_URL/rockstar/coreapi-microservice:0.0.1

oc login $OPENSHIFT_URL --token=$OPENSHIFT_TOKEN
oc project rockstar
oc delete -f kubernetes/
oc create -f kubernetes/