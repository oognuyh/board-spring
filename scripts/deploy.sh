#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=board-spring

echo "> Copy build file"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> Check pid of the currently running application"

CURRENT_PID=${pgrep -fl ${PROJECT_NAME} | grep jar | awk '{print $1}')

if [ -z "$CURRENT_PID" ]; then
    echo "> No application is currently running"
else
    echo "> Kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> Deploy new application"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR name: $JAR_NAME"

echo "> Give execute permission to $JAR_NAME"

chmod +x $JAR_NAME

nohup java -jar \
    -Dspring.config.location=classpath:/application.yaml,classpath:/application-dev.yaml,/home/ec2-user/app/application-real.yaml \
    $JAR_NAME > $REPOSITORY/nohup.out  2>&1 &
