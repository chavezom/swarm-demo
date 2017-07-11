#!/bin/sh

$(dirname $0)/undeploy.sh
sleep 6
eval  docker ${docker_config_swarm_node_1} volume rm jenkins_jenkins-home
