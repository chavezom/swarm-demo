#!/bin/bash

# get the access token for the allowed user

if [ ! -z "$(eval docker ${docker_config_swarm_node_1} \
             secret ls  --filter Name=GITHUB_TOKEN -q)" ]; then
    eval docker ${docker_config_swarm_node_1} \
         secret rm GITHUB_TOKEN
fi
echo -n "GitHub Token for admin auth: "
read -sr GITHUB_TOKEN
echo "${GITHUB_TOKEN}" | \
  eval  docker ${docker_config_swarm_node_1} \
        secret create GITHUB_TOKEN -

unset GITHUB_TOKEN


# get the application client id

if [ ! -z "$(eval docker ${docker_config_swarm_node_1} \
             secret ls  --filter Name=GITHUB_CLIENT_ID -q)" ]; then
    eval docker ${docker_config_swarm_node_1} \
         secret rm GITHUB_CLIENT_ID
fi
echo -n "GitHub Jenkins App Client ID: "
read -sr GITHUB_CLIENT_ID
echo "${GITHUB_CLIENT_ID}" | \
  eval  docker ${docker_config_swarm_node_1} \
        secret create GITHUB_CLIENT_ID -

unset GITHUB_CLIENT_ID

# get the application client secret

if [ ! -z "$(eval docker ${docker_config_swarm_node_1} \
             secret ls  --filter Name=GITHUB_CLIENT_SECRET -q)" ]; then
    eval docker ${docker_config_swarm_node_1} \
         secret rm GITHUB_CLIENT_SECRET
fi
echo -n "GitHub Jenkins App Client SECRET: "
read -sr GITHUB_CLIENT_SECRET
echo "${GITHUB_CLIENT_SECRET}" | \
  eval  docker ${docker_config_swarm_node_1} \
        secret create GITHUB_CLIENT_SECRET -

unset GITHUB_CLIENT_SECRET

# get the list of admin users for github

if [ ! -z "$(eval docker ${docker_config_swarm_node_1} \
             secret ls  --filter Name=GITHUB_ADMIN_USERS -q)" ]; then
    eval docker ${docker_config_swarm_node_1} \
         secret rm GITHUB_ADMIN_USERS
fi
echo -n "GitHub Jenkins Admin user list, seperate commas: "
read -sr GITHUB_ADMIN_USERS
echo "${GITHUB_ADMIN_USERS}" | \
  eval  docker ${docker_config_swarm_node_1} \
        secret create GITHUB_ADMIN_USERS -

unset GITHUB_ADMIN_USERS
