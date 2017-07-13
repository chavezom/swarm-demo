#!/bin/sh

eval  "SWARM_MASTER_IP=${docker_swarm_ip_swarm_node_1} \
        docker ${docker_config_swarm_node_1} \
            stack deploy -c ./stack.yml jenkins"
