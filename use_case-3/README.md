# Introduction

Lets create API Service to store we might create with an app.  We'll use Minio.io to demonstrate how swarm can handle services that require storage.

We don't need shared volume storage because Minio handles the replication for us.

## Setup

https://docs.minio.io/docs/deploy-minio-on-docker-swarm

```
echo "AKIAIOSFODNN7EXAMPLE" | docker secret create access_key -
echo "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY" | docker secret create secret_key -
```

```
docker service create --name="minio-service" --secret="access_key" --secret="secret_key" minio/minio server /export
```


## Test

## Exercise

1. Setup volume storage for the Minio service

1. Setup an nginx proxy so we can hit any Minio service end-point

1. Monitor the Minio service health with the Swarm healthcheck feature

## Un-deploy
