#!/usr/bin/env bash

source "$(dirname "$_")/set_options.env"

# Setup keys to connect
for i in $(seq 1 $NODE_COUNT); do
    mkdir -p "${SCRIPT_DIR}/../keys/${NODE_INSTANCE}-${i}"
    for option in $(docker-machine config "${NODE_INSTANCE}-${i}" | grep 'tls' | grep '='); do
        echo "copying -> $option"
        eval "cp $(echo $option| awk -F'=' '{print $2}') \"${SCRIPT_DIR}/../keys/${NODE_INSTANCE}-${i}/\""
    done
done

echo 'local _pwd=$(cd "$(dirname "$0")"; pwd)' > "${SCRIPT_DIR}/../keys/swarm-alias.sh"
for i in $(seq 1 $NODE_COUNT); do
    node_ip="$(docker-machine ip ${NODE_INSTANCE}-${i})"
    cat > "${SCRIPT_DIR}/../keys/${NODE_INSTANCE}-${i}/alias_connect.sh" << SCRIPT
#!/bin/bash

_script="\$_"
local _cert_dir=\$(cd "\$(dirname "\${_script}")"; pwd)
export docker_cert_dir_$(echo ${NODE_INSTANCE}-${i}| sed 's/\-/_/g')="\${_cert_dir}"
export docker_swarm_ip_$(echo ${NODE_INSTANCE}-${i}| sed 's/\-/_/g')="${node_ip}"
export docker_config_$(echo ${NODE_INSTANCE}-${i}| sed 's/\-/_/g')='--tlsverify --tlscacert="'\${_cert_dir}'/ca.pem" --tlscert="'\${_cert_dir}'/cert.pem" --tlskey="'\${_cert_dir}'/key.pem" -H=tcp://${node_ip}:2376'
echo alias docker-${NODE_INSTANCE}-${i}='docker \${docker_config_$(echo ${NODE_INSTANCE}-${i}| sed 's/\-/_/g')}'
eval alias "docker-${NODE_INSTANCE}-${i}='docker \${docker_config_$(echo ${NODE_INSTANCE}-${i}| sed 's/\-/_/g')}'"

SCRIPT
    cat >> "${SCRIPT_DIR}/../keys/swarm-alias.sh" << SCRIPT
source "\${_pwd}/${NODE_INSTANCE}-${i}/alias_connect.sh"
SCRIPT
done


echo "done saving keys"
