#!/usr/bin/env bash
cd ../MsgConnection
mvn clean install

cd ../DataBase
mvn clean install

cd ../L13
mvn clean package