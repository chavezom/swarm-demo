#!/usr/bin/env bash

source "$(dirname "$_")/set_options.env"

# Validate all machines are running
for i in $(seq 1 $NODE_COUNT); do
    echo "Validating ${NODE_INSTANCE}-${i}"
    docker $(docker-machine config ${NODE_INSTANCE}-${i}) info > /dev/null
done

echo "done with validate"
