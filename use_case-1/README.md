# Introduction

First use case will show how we can start a new service for key/value store.  This demo uses Hashicorp Consul and [GitHub users sdelrio autopilot demo image](https://github.com/sdelrio/consul).

## Setup

1. source the alias keys
   ```
   source ../keys/swarm-alias.sh
   ```
1. deploy consul
   ```
   make deploy
   ```
1. get the status and wait for a leader to be elected
   ```
   make status
   ```

## Test

Validate that consul agent is running
```
curl http://$(docker-machine ip swarm-node-1):8500
```

## Exercise

1. Swarm is using the labeled nodes, show where consul processes are running
   ```
   docker-swarm-node-1 stack services consul
   docker-swarm-node-1 stack ps consul
   ```

1. Swarm provides desired state on services, kill and remove a service and watch the service recover

1. Scale the consul service by adding another quorum label to a new node

## Un-deploy

```
docker-swarm-node-1 stack rm consul
```
