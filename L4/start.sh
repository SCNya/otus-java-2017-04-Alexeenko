#!/usr/bin/env bash
MEMORY="-Xms2G -Xmx2G -XX:MaxMetaspaceSize=256m"

GC1="-XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=10 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark -XX:+UseParNewGC"
GC2="-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+UseAdaptiveSizePolicy -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=10"
GC3="-XX:+UseSerialGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=10"
GC4="-XX:+UseG1GC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=10"

LOGGER="-Djava.util.logging.SimpleFormatter.format="%1$tc %n%4$s: %5$s%6$s%n""

GC_LOG="-verbose:gc -Xloggc:./logs/gc_pid_%p.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=2 -XX:GCLogFileSize=10M"

DUMP="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps/"

rm -f -r ./logs
mkdir ./logs
rm -f -r ./dumps
mkdir ./dumps

java $MEMORY $GC1 $LOGGER $GC_LOG $DUMP -XX:OnOutOfMemoryError="kill -3 %p" -jar target/L4.jar > jvm.out  #ParNew
java $MEMORY $GC2 $GC_LOG $DUMP -XX:OnOutOfMemoryError="kill -3 %p" -jar target/L4.jar > jvm.out  #ParallelOld
java $MEMORY $GC3 $GC_LOG $DUMP -XX:OnOutOfMemoryError="kill -3 %p" -jar target/L4.jar > jvm.out  #Serial
java $MEMORY $GC4 $GC_LOG $DUMP -XX:OnOutOfMemoryError="kill -3 %p" -jar target/L4.jar > jvm.out  #G1