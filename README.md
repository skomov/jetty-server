# jetty-server

## compile
mvn package

## run
java -jar jetty-server.jar

## example
$ curl -X POST -d '{"value" : "v4"}' http://localhost:8888  
{"key":"v4"}
