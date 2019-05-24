# TODO

/////////////// Wildfly ///////////////
./../../../../Tools/jboss/wildfly-10.1.0.Final/bin/standalone.sh
http://localhost:9990/
 Edgar/dark

/////////////// Mongo ///////////////
./../../../../Tools/mongodb/bin/mongod --auth

db.createUser( { user: "root", pwd: "password", roles: [ { role: "userAdminAnyDatabase", db: "admin" }, { role: "readWriteAnyDatabase", db: "admin" } ] } )

/////////////// Docker ///////////////
docker build . -t catering_java_api:latest
docker images
docker ps -all

docker run -it [IMAGE_ID] /bin/sh
docker rm $(docker ps -a -q)
docker rm [CONTAINER_ID]
docker rmi [IMAGE_ID]

docker run -e PROFILE=h2 -e PORT=8000 -p 8000:8000 [IMAGE_ID]