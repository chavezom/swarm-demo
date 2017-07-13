# Introduction

Lets create API Service to store we might create with an app.  We'll use Minio.io to demonstrate how swarm can handle services that require storage.

We don't need shared volume storage because Minio handles the replication for us.

## Setup

You can find detailed instructions on how this is setup here:
https://docs.minio.io/docs/deploy-minio-on-docker-swarm

1. source the alias keys
   ```
   source ../keys/swarm-alias.sh
   ```

1. Create the secrets required to run minio.
   ```
   make secrets
   ```
2. Deploy minio.
   ```
   make deploy
   ```
3. You can get the keys from the logs by executing :
   ```
   make status
   ```

## Test

Pull up minio url locally on port `9001`: `http://192.168.99.101:9001`

## Exercise

1. Setup volume storage for the Minio service

1. Mesh networking takes care of discovery for us, demo that

1. Minio shares network overlay to talk and replicate data, demo that

## Un-deploy

```
make undeploy
```
