#!/bin/sh

eval  docker ${docker_config_swarm_node_1} stack deploy -c ./stack.yml minio
