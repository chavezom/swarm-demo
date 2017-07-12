#!/bin/bash

eval docker-compose -f ./docker-compose-slave.yml ${docker_config_swarm_node_1} build docker-v1.13
eval docker-compose -f ./docker-compose-slave.yml ${docker_config_swarm_node_1} build docker-v17.06

eval docker-compose -f ./docker-compose-slave.yml ${docker_config_swarm_node_2} build docker-v1.13
eval docker-compose -f ./docker-compose-slave.yml ${docker_config_swarm_node_2} build docker-v17.06

eval docker-compose -f ./docker-compose-slave.yml ${docker_config_swarm_node_7} build docker-v1.13
eval docker-compose -f ./docker-compose-slave.yml ${docker_config_swarm_node_7} build docker-v17.06
