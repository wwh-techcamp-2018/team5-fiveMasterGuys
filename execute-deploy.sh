#!/bin/bash
REPOSITORY=/home/ec2-user/app/travis

. /home/ec2-user/.bash_profile

echo "current profile : $SERVER_PROFILE"

echo "> Checking exist process id ..."
CURRENT_PID=$(pgrep -f recipehub)

if [ -z $CURRENT_PID ]; then
    echo "> Can't find exist process."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

JAR_NAME=$(ls $REPOSITORY/jar/ | grep 'recipehub' | tail -n 1)
echo "> Copying new bundle ..."
echo "> Copy $JAR_NAME to $REPOSITORY/jar"
cp $REPOSITORY/build/build/libs/*.jar $REPOSITORY/jar/

echo "> Starting application with background ..."
nohup java -jar $REPOSITORY/jar/$JAR_NAME > /dev/null 2>&1 &