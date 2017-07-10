#!/usr/bin/env bash

source "$(dirname "$_")/set_options.env"

# Initialize the first node
if [ -z "$(docker $(docker-machine config ${NODE_INSTANCE}-1) \
                                 swarm join-token worker -q 2>/dev/null)" ]; then
  docker $(docker-machine config ${NODE_INSTANCE}-1) \
      swarm init --advertise-addr $(docker-machine ip ${NODE_INSTANCE}-1)
else
  echo "skipping swarm init"
fi

echo "done with swarm init"
