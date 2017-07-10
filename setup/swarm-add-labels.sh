#!/usr/bin/env bash

source "$(dirname "$_")/set_options.env"

# Give the last 3 a quorum label for this demo
for i in $(seq $NODE_COUNT -1 1|head -3); do
    docker $(docker-machine config ${NODE_INSTANCE}-1) \
        node update --label-add quorum=true "${NODE_INSTANCE}-${i}"
done
# Remaining nodes should not have the quorum label
for i in $(seq $(( $NODE_COUNT - 3 ))); do
    if [ "$(docker $(docker-machine config ${NODE_INSTANCE}-1) \
        node inspect "${NODE_INSTANCE}-${i}" --format \
        '{{ range $key, $value := .Spec.Labels }}{{ if eq $key "quorum" }}yes{{end}}{{ end }}')" = "yes" ]; then
        docker $(docker-machine config ${NODE_INSTANCE}-1) \
            node update --label-rm quorum "${NODE_INSTANCE}-${i}"
    fi
done

# show all labels
for i in $(seq 1 $NODE_COUNT); do
    if [ "$(docker $(docker-machine config ${NODE_INSTANCE}-1) \
        node inspect "${NODE_INSTANCE}-${i}" --format \
        '{{ range $key, $value := .Spec.Labels }}{{ if eq $key "quorum" }}yes{{end}}{{ end }}')" = "yes" ]; then
          echo "Label quorum exist on ${NODE_INSTANCE}-${i}"
    fi
done

echo "done with adding swarm labels"
