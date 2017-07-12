#!/bin/bash

# setup secrets
swarm_master="${1:-${docker_config_swarm_node_1}}"

function clean_secret {
    local key_name="${1}"
    if [ ! -z "$(eval "docker ${swarm_master} \
                secret ls  --filter Name=${key_name} -q")" ]; then
        eval "docker ${swarm_master} \
                secret rm ${key_name}"
    fi
    unset key_name
}

function save_secret {
    local key_name="${1}"
    local message=${2}
    if [ -z "${3}" ]; then
        echo -n "${message}"
        read -sr value
    else
        value="${3}"
    fi
    [ ! -z "${value}" ] && \
        echo "${value}" | \
            eval  "docker ${swarm_master} \
                secret create ${key_name} -"

     echo ""
     unset value
     unset key_name
     unset message
}

save_secret 'access_key' "Minio Access Token for admin auth: " \
    "$(date | md5sum | awk '{print $1}' | cut -c1-19)"

save_secret 'secret_key' "Minio Secret Token for admin auth: " \
    "$(openssl rand -base64 32)"
