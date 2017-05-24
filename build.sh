#!/bin/sh
VERSION=$1
fail=0
set -x

if [ -n "${VERSION}" ]
then
    mvn versions:set -DgenerateBackupPoms=false -DnewVersion=${VERSION}
fi

echo "CURRENT DIR: $PWD"
ls -latr $PWD
docker pull ppatierno/qdrouterd:0.8.0-repo || fail=1
container=`sudo docker run -d --name qdrouterd -p 5672:5672 -p 55673:55673 -v $PWD/src/test/resources:/conf:z ppatierno/qdrouterd:0.8.0-repo qdrouterd -c /conf/qdrouterd.conf`
echo "CONTAINER: $container"
sleep 10
echo "Docker images:" 
docker ps -a
echo "Logs: "
docker logs $container


trap "docker stop qdrouterd; docker rm qdrouterd" EXIT

mvn test package -B || fail=1

exit $fail
