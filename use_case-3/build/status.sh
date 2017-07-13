#!/bin/sh

eval "docker ${docker_config_swarm_node_1} service logs minio_minio1"
eval "docker ${docker_config_swarm_node_1} service logs minio_minio2"
eval "docker ${docker_config_swarm_node_1} service logs minio_minio3"
eval "docker ${docker_config_swarm_node_1} service logs minio_minio4"
eval "docker ${docker_config_swarm_node_1} stack ps minio|grep -i 'running'"

echo "EndPoints used minio1"
eval "docker ${docker_config_swarm_node_1} service inspect minio_minio1 --format '{{.Endpoint.Ports}}'"
echo "EndPoints used minio2"
eval "docker ${docker_config_swarm_node_1} service inspect minio_minio2 --format '{{.Endpoint.Ports}}'"
echo "EndPoints used minio3"
eval "docker ${docker_config_swarm_node_1} service inspect minio_minio3 --format '{{.Endpoint.Ports}}'"
echo "EndPoints used minio4"
eval "docker ${docker_config_swarm_node_1} service inspect minio_minio4 --format '{{.Endpoint.Ports}}'"
