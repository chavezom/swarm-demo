#!/bin/bash

for s in $(eval "docker ${docker_config_swarm_node_1} node ls --format '{{.Hostname}}'"|sed 's/-/_/g'); do
  node_config=$(eval "echo \${docker_config_${s}}")
  eval "docker-compose \
          -f ./stack.yml \
          ${node_config} \
          build ${BUILD_ARGS} vault"
done
