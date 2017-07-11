#!/bin/bash

eval docker-compose -f ./stack.yml ${docker_config_swarm_node_1} build jenkins

