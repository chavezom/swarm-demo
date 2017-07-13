#!/bin/bash

node_config=$(eval "docker ${docker_config_swarm_node_1} \
                        stack ps vault --format '{{.Node}}:{{.CurrentState}}'" \
                            |grep 'Running' \
                            | awk -F ':' '{print $1}' \
                            |sed 's/-/_/g')
if [ ! -z "${no_proxy}" ]; then
  eval "export no_proxy=${no_proxy},\${docker_swarm_ip_${node_config}}"
  export NO_PROXY=$no_proxy
fi
cid=$(eval "docker \${docker_config_${node_config}} \
            ps --filter Name=vault_vault -q")
eval "docker \${docker_config_${node_config}} \
          exec -it ${cid} /usr/local/bin/init.sh"
