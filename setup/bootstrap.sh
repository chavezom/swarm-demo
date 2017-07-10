#!/usr/bin/env bash

source "$(dirname "$_")/set_options.env"
"${SCRIPT_DIR}/create.sh"
"${SCRIPT_DIR}/validate.sh"
"${SCRIPT_DIR}/swarm-init.sh"
"${SCRIPT_DIR}/swarm-join-nodes.sh"
"${SCRIPT_DIR}/swarm-promote-managers.sh"
"${SCRIPT_DIR}/savekeys.sh"
"${SCRIPT_DIR}/swarm-add-labels.sh"

# Show the swarm cluster
docker $(docker-machine config ${NODE_INSTANCE}-1) node ls
