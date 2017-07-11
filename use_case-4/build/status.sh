#!/bin/sh

eval  docker ${docker_config_swarm_node_1} stack ps jenkins

id=$(eval  docker ${docker_config_swarm_node_1} stack ps jenkins |grep 'Running'|awk '{print $1}')
eval docker ${docker_config_swarm_node_1} service logs $id
