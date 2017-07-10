#!/usr/bin/env bash

source "$(dirname "$_")/set_options.env"

for i in $(seq 1 $NODE_COUNT); do
    echo "Destroying machine - swarm-node-${i}"
    if [ ! -z "$(docker-machine ip "swarm-node-${i}" 2>/dev/null)" ]; then
      eval "docker-machine rm -f -y swarm-node-${i}"
    else
        echo "...skipping, already removed"
    fi
done
