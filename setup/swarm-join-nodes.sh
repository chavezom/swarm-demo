#!/usr/bin/env bash

source "$(dirname "$_")/set_options.env"

# Get swarm join token
join_token=$(docker $(docker-machine config ${NODE_INSTANCE}-1) \
                swarm join-token worker -q)
# Join all as workers
for i in $(seq 2 $NODE_COUNT); do
    if [ -z "$(docker $(docker-machine config ${NODE_INSTANCE}-1) \
                node inspect \
                "${NODE_INSTANCE}-${i}" --pretty 2>/dev/null)" ]; then
        docker $(docker-machine config ${NODE_INSTANCE}-${i}) \
            swarm join --token "${join_token}" \
            $(docker-machine ip ${NODE_INSTANCE}-1):2377
    else
        echo "skipping join, node already exist"
    fi
done

echo "done with swarm join nodes"
