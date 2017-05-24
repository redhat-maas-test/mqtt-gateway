#!/bin/sh

if [ -d /etc/mqtt-gateway/ssl ]; then
	export ENMASSE_MQTT_SSL=true
	export ENMASSE_MQTT_KEYFILE='/etc/mqtt-gateway/ssl/server-key.pem'
	export ENMASSE_MQTT_CERTFILE='/etc/mqtt-gateway/ssl/server-cert.pem'
	export ENMASSE_MQTT_LISTENPORT=8883
fi

. /usr/local/dynamic-resources/dynamic_resources.sh
MAX_HEAP=`get_heap_size`
if [ -n "$MAX_HEAP" ]; then
	export JAVA_OPTS="-Xms${MAX_HEAP}m -Xmx${MAX_HEAP}m $JAVA_OPTS"
fi

exec java $JAVA_OPTS -Dvertx.disableFileCaching=true -Dvertx.disableFileCPResolving=true -jar /opt/mqtt-gateway/mqtt-gateway.jar