#!/bin/sh

$(dirname $0)/undeploy.sh
sleep 3
eval  docker ${docker_config_swarm_node_1} volume rm jenkins_jenkins-home


