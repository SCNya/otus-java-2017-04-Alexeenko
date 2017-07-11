#!/usr/bin/env bash

cd ../DataBase

mvn clean install

cd ../L10

mvn clean package

cp target/L10.war ~/apps/jetty-distribution-9.4.6.v20170531/webapps/root.war