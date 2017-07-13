#!/bin/sh


id=$(eval  docker ${docker_config_swarm_node_1} stack ps jenkins |grep 'Running'|awk '{print $1}')
eval "docker ${docker_config_swarm_node_1} service logs $id"

eval "docker ${docker_config_swarm_node_1} stack ps jenkins"
echo "EndPoints used"
eval "docker ${docker_config_swarm_node_1} service inspect jenkins_jenkins --format '{{.Endpoint.Ports}}'"
