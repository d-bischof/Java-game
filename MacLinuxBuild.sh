#!bin/bash

javac -d build $(find src -name "*.java") && jar cfm sprint.jar <(echo "Main-Class: Sprint") -C build . && java -jar sprint.jar
