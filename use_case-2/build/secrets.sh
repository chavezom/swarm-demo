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
function save_secret_c {
    local key_name="${1}"
    local message=${2}
    if [ -z "${3}" ]; then
        echo -n "${message}"
        read -r value
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

function save_secret_f {
    local key_name="${1}"
    local message=${2}
    if [ -z "${3}" ]; then
        echo -n "${message}"
        read -r value
    else
        value="${3}"
    fi
    [ ! -z "${value}" ] && \
        eval  "docker ${swarm_master} \
                secret create ${key_name} \"${value}\""

     unset value
     unset key_name
     unset message
}

# we'll rely on the swarm keys to be sourced
save_secret_f 'vault_tls_key' "Vault TLS key path: " \
    "${docker_cert_dir_swarm_node_1}/key.pem"

save_secret_f 'vault_tls_cert' "Vault TLS key cert: " \
    "${docker_cert_dir_swarm_node_1}/cert.pem"

# create VAULT Secret for GITHUB Auth
save_secret 'VAULT_GITHUB_TOKEN' "Vault GitHub Token for admin auth: "
save_secret_c 'VAULT_GITHUB_ADMIN_ORG' "Vault GitHub Admin Org for auth: "
