#!/bin/sh

$(dirname $0)/undeploy.sh
sleep 6
volumes="minio_minio1-data
         minio_minio2-data
         minio_minio3-data
         minio_minio4-data"
for s in $(eval "docker ${docker_config_swarm_node_1} node ls --format '{{.Hostname}}'"|sed 's/-/_/g'); do
    for v in $( echo "${volumes}" ); do
        node_config=$(eval "echo \${docker_config_${s}}");
        if [ ! -z "$(eval "docker ${node_config} \
                              volume ls -q | grep $v")" ]; then
            eval  "docker ${node_config} \
                      volume rm $v"
        fi
    done
done
