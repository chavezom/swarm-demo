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
    echo -n "${message}"
    read -sr value
    [ ! -z "${value}" ] && \
        echo "${value}" | \
            eval  "docker ${swarm_master} \
                secret create ${key_name} -"

     echo ""
     unset value
     unset key_name
     unset message
}

# clean_secret 'GITHUB_TOKEN'
save_secret 'GITHUB_TOKEN' "GitHub Token for admin auth: "

# clean_secret 'GITHUB_CLIENT_ID'
save_secret 'GITHUB_CLIENT_ID' "GitHub Jenkins App Client ID: "

# clean_secret 'GITHUB_CLIENT_SECRET'
save_secret 'GITHUB_CLIENT_SECRET' "GitHub Jenkins App Client SECRET: "

# clean_secret 'GITHUB_ADMIN_USERS'
save_secret 'GITHUB_ADMIN_USERS' "GitHub Jenkins Admin user list, seperate commas: "
