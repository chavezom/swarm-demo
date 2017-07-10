# Introduction

Bootstrap a swarm cluster using swarm-mode with Docker Toolbox.
This cluster will be used in several exercises for different use
cases that demonstrate some of the fundamental Docker swarm-mode
features.

## First time run

Setup the first cluster with the following script.
```
./setup/bootstrap.sh
```
By default this will create 7 nodes with 3 managers and 4 workers.
It will also place a label called `quorum=true` on 3 of the worker nodes. As a final step, a set of alias and cert keys will be copied
to a directory called `keys`. On a bash shell prompt you can
source `swarm-alias.sh` to get access to any swarm node.

```
source ./keys/swarm-alias.sh
```

## other tools

- `./setup/start.sh` - useful for running after system reboot to re-sync and startup all swarm nodes again.
- `./setup/reset.sh` - remove all swarm nodes, including managers and set them back up.
- `./setup/destroy.sh` - remove all docker-machine nodes.
