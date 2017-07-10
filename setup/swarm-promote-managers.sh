#!/usr/bin/env bash

source "$(dirname "$_")/set_options.env"

# Promote two as managers to give a quorum
for i in $(seq 2 $NODE_MANAGER_COUNT); do
    if [ -z "$(docker $(docker-machine config ${NODE_INSTANCE}-1) \
                  node inspect "${NODE_INSTANCE}-${i}"|grep 'Manager' 2>/dev/null)" ]; then
        docker $(docker-machine config ${NODE_INSTANCE}-1) \
            node promote "${NODE_INSTANCE}-${i}"
    else
        echo "skipping promote, already a manager"
    fi
done

echo "done with swarm promoting managers"
