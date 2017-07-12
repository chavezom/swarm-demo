#!/usr/bin/env bash

source "$(dirname "$_")/set_options.env"

TIME_SERVER="${TIME_SERVER:-time.nist.gov}"
# sync time on each machine so that we can generate
# keys that are good
if [ ! -z "${TIME_SERVER}" ]; then
    for i in $(seq 1 $NODE_COUNT); do
        echo "syncing time on ${NODE_INSTANCE}-${i}"
        # echo eval "docker-machine ssh "${NODE_INSTANCE}-${i}" 'sudo ntpclient -s -h ${TIME_SERVER}'"
        eval "docker-machine ssh "${NODE_INSTANCE}-${i}" 'sudo ntpclient -s -h ${TIME_SERVER}'"
    done
fi

# Validate all machines are running
for i in $(seq 1 $NODE_COUNT); do
    echo "Validating ${NODE_INSTANCE}-${i}"
    docker $(docker-machine config ${NODE_INSTANCE}-${i}) info > /dev/null
done

echo "done with validate"
