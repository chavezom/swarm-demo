#!/bin/sh

eval "docker ${docker_config_swarm_node_1} service logs minio_minio1"
eval "docker ${docker_config_swarm_node_1} service logs minio_minio2"
eval "docker ${docker_config_swarm_node_1} service logs minio_minio3"
eval "docker ${docker_config_swarm_node_1} service logs minio_minio4"
eval "docker ${docker_config_swarm_node_1} stack ps minio|grep -i 'running'"
