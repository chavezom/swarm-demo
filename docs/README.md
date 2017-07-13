# Use Case 1 - Simple Key Value Store

## Setup

Simple key value stores can be setup with Hashicorp Consul so that
we can create a highly available quorum service for discovery.

- use swarm to label nodes and maintain the quorum
- use swarm services to maintain desired state for availability
- use swarm scheduling rules to automatically scale the services

[read more ... ](../use_case-1/README.md)

# Use Case 2 - Lets Add Secrets

## Setup

A policy driven secrets management system can help us establish tenancy
capabilities in other application frame works and services that we decide to
build.  We can leverage Hashicorp Vault for this task.

- custom startup scripts can be used to securly startup and unseal the vault
- Use the secrets store to store more than just passwords

[read more ... ](../use_case-2/README.md)

# Use Case 3 - Stateless API Service

## Setup

API Services that work with each other to provide persistent stateless services can be very powerful.  Here we show how to leverage minio api services to offer just that, and simulate an S3 backend storage system with volumes.

- use local storage volumes out of the box to create a persistent object store
- swarm uses mesh networking services to find the available api services where ever they might be running

[read more ... ](../use_case-3/README.md)

# Use Case 4 - Scheduling

## Setup

Scheduling with swarm can be acommplished with Jenkins.  Jenkins provides a way to fully automate the Jenkins configuration and job management in a single stack file.

- use Jenkins to schedule jobs and run docker containers with in docker containers
- manage all configuration aspects of Jenkins from code and schedule other services as jobs

[read more ... ](../use_case-4/README.md)
