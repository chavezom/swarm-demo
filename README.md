# Introduction

This is a use case demo showcasing some of the features in swarm.  We'll use this for a presentation that we are providing for one of our first Docker Meetup groups in Sacramento.

![Docker Scaramento Meetup Group](docs/images/docker_sacramento.png)

## Requirements

1. You'll need [Docker Toolbox](https://github.com/docker/toolbox/releases)
1. Download the [GitHub Desktop](https://desktop.github.com/) which comes with git and a bash client.
1. A system with about ~16G of RAM, and 4 Cores.  SSD drive is highly desirable.  An internet connection with 10 mBits / sec or better.

## Time Estimate

- Allow 60 minutes for swarm cluster provisioning.
- Allow 30 minutes to execute the demo.

## How to prepare
1. Install all requirements: Docker Toolbox and GitHub Desktop
1. Clone this repository : `git clone https://github.com/wenlock/swarm-demo`
1. If you have a proxy, setup the proxy.env file with the following contents:
   ```
   PROXY=yourproxy.com:8080
   ```
   Source both the `proxy.env` and `proxy.sh` before running the `bootstrap.sh` script.
1. Run the demo bootstrap script: `bash setup/bootstrap.sh`

## What have we done so far and whats next?

## Running the demo

Now lets do [swarm use cases](docs/README.md)!

## Trouble shoot

### docker-machine complains about certs

Sometime you might get this error:
```
docker-swarm-node-1 node ls
error during connect: Get https://192.168.99.101:2376/v1.29/nodes: x509: certificate is valid for 192.168.99.100, not 192.168.99.101
```
This might be a good indicator it's time to regenerate your certs
because the time or ip addresses are wrong due to boot sequence.
Usually this occurs after reboot.

This script should make sure your cluster is up and running and
will regenerate the certs automatically.
```
setup/start.sh
```
