# jetty-server

## compile
mvn package

## run
java -jar target/jetty-server-1.0-SNAPSHOT.jar

## example
$ curl -X POST -d '{"key" : "v4"}' http://localhost:8888  
{"key":"v4"}
