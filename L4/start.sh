#!/usr/bin/env bash
MEMORY="-Xms2G -Xmx2G -XX:MaxMetaspaceSize=256m"

GC1="-XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=10 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark -XX:+UseParNewGC"
GC2="-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+UseAdaptiveSizePolicy -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=10"
GC3="-XX:+UseSerialGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=10"
GC4="-XX:+UseG1GC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=10"

LOGGER="-Djava.util.logging.SimpleFormatter.format="%1$tc %n%4$s: %5$s%6$s%n""

JAVA_VERSION="$(java -version 2>&1 | grep -i version | cut -d'"' -f2 | cut -d'.' -f1-2)"

LOG_JAVA_8="-verbose:gc -Xloggc:./logs/gc_pid_%p.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=2 -XX:GCLogFileSize=1M"
LOG_JAVA_9="-Xlog:gc:file=./logs/gc_pid_%p.log:uptimemillis:filecount=2,filesize=1m"

if [[ $JAVA_VERSION =~ 9 ]]
then
     echo "Java" $JAVA_VERSION
     GC_LOG=$LOG_JAVA_9
  else
    echo "Java" $JAVA_VERSION
    GC_LOG=$LOG_JAVA_8
  fi


DUMP="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps/"

rm -f -r ./logs
mkdir ./logs
rm -f -r ./dumps
mkdir ./dumps

for GC in $GC1 $GC2 $GC3 $GC4
do
  java $MEMORY $GC $LOGGER $GC_LOG $DUMP -XX:OnOutOfMemoryError="kill -3 %p" -jar target/L4.jar > jvm.out
done