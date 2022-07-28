#!/bin/bash

source /etc/environment

java -jar target/intrusion.detector-0.0.1-SNAPSHOT.jar \
--server.port=8081 \
--spring.datasource.url=jdbc:mysql://${AWS_DB_IP}:3306/PiTelemetry \
--spring.datasource.username=${AWS_DB_USERNAME} \
--spring.datasource.password=${AWS_DB_PASSWORD}