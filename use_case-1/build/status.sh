#!/bin/sh


eval "docker ${docker_config_swarm_node_1} \
        service logs consul_consul"
eval "docker ${docker_config_swarm_node_1} \
        stack ps consul"
