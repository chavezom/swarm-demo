#!/bin/sh


eval "docker ${docker_config_swarm_node_1} \
        service logs vault_vault"
eval "docker ${docker_config_swarm_node_1} \
        stack ps vault"

echo "EndPoints used vault"
eval "docker ${docker_config_swarm_node_1} service inspect vault_vault --format '{{.Endpoint.Ports}}'"
