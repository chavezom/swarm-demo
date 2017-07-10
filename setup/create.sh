#!/usr/bin/env bash

source "$(dirname "$_")/set_options.env"

# create a machine for each node
for i in $(seq 1 $NODE_COUNT); do
    echo "Creating machine - ${NODE_INSTANCE}-${i}"
    if [ -z "$(docker-machine ip "${NODE_INSTANCE}-${i}" 2>/dev/null)" ]; then
      eval "docker-machine create -d virtualbox \
           ${PROXY_OPTIONS} \
          --virtualbox-cpu-count 1  \
          --virtualbox-memory 2048  \
          --virtualbox-disk-size 30000 \
          --virtualbox-no-vtx-check ${NODE_INSTANCE}-${i}"
    else
        echo "...skipping, already exist"
    fi
done

echo "done with create nodes"
