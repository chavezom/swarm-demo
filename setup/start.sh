#!/usr/bin/env bash


source "$(dirname "$_")/set_options.env"

# Make sure all nodes are started
for i in $(seq 1 $NODE_COUNT); do
    echo "Checking node - ${NODE_INSTANCE}-${i}"
    if [ -z "$(docker-machine ip "${NODE_INSTANCE}-${i}" 2>/dev/null)" ]; then
      docker-machine start "${NODE_INSTANCE}-${i}"
    else
        echo "...skipping, already started"
    fi
done

"${SCRIPT_DIR}/regenerate.sh"

docker-machine ls
