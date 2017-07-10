#!/usr/bin/env bash


source "$(dirname "$_")/set_options.env"

# Regenerate all certs
for i in $(seq 1 $NODE_COUNT); do
      docker-machine regenerate-certs "${NODE_INSTANCE}-${i}" -force
done
