#!/usr/bin/env bash

source "$(dirname "$_")/set_options.env"

# Reset all nodes and re-run bootstrap
"${SCRIPT_DIR}/start.sh"

# Join all as workers
for i in $(seq 1 $NODE_COUNT); do
    docker $(docker-machine config ${NODE_INSTANCE}-${i}) \
        swarm leave --force || echo "ignore error"
done

"${SCRIPT_DIR}/bootstrap.sh"

echo "done with swarm reset"
