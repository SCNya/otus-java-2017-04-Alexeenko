#!/usr/bin/env bash
cd ../MsgConnection
mvn clean install

cd ../DataBase
mvn clean package

cd ../Frontend
mvn clean package

cp target/Frontend.war ../L12/jetty/webapps/root.war

cd ../L12
mvn clean package