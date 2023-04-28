#!/bin/bash


SUB=".xlsx"
request="-requestlic"
generate="-generatelic"
ScriptQA="ScriptQA"
ScriptUAT="ScriptUAT"



if [ -z "$1" -a -z "$2" ]
then
        mvn test -Dconfig="Default" -DmasterFile="Master.xlsx" #echo default case both null
elif [[ "$1" == *"$SUB"* ]]; then
        mvn test -Dconfig="Default" -DmasterFile=$1 #echo master case first parameter is file second parameter is null
elif [[ "$1" == "$request" ]]; then
        java -cp ./lib/daisy-core-browser-base-command-0.0.1.jar com.persistent.daisy.lic.saltencrypt.UtilEncrypt #echo "This is request lic loop"
elif [[ "$1" == "$generate" ]]; then
	java -cp ./lib/mysql-connector-java-8.0.28.jar:./lib/daisy-core-browser-base-command-0.0.1.jar com.persistent.daisy.lic.saltencrypt.UtilDecrypt 
	 #echo "This is generate lic loop"
elif [ ! -z "$1" ] && [ ! -z "$2" ]; then 	
	echo "This is both (environment and file present)"
        mvn test -Dconfig=$1 -DmasterFile=$2 #echo both present
elif [[ "$1" == "$ScriptQA" ]]; then
	mvn test -Dconfig="QA" -DmasterFile=Master.xlsx #This is for "ScriptQA" execution.
elif [[ "$1" == "$ScriptUAT" ]]; then
	mvn test -Dconfig="UAT" -DmasterFile=Master.xlsx #This is for "ScriptUAT" execution.
elif [ "$1" != *"$SUB"* ] && [ -z "$2" ]; then
        mvn test -Dconfig=$1 -DmasterFile="Master.xlsx" #echo first case environment present and second is master file
fi
