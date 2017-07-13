#!/bin/sh


eval "docker ${docker_config_swarm_node_1} \
        service logs consul_consul"
eval "docker ${docker_config_swarm_node_1} \
        stack ps consul"

echo " -- EndPoints used consule"
eval "docker ${docker_config_swarm_node_1} service inspect consul_consul --format '{{.Endpoint.Ports}}'"
