# Introduction

Lets add a secrets system to Consul.  For this we'll use Hashicorp Vault.
We will use the swarm vault store as the primary method to secure the global key store required to start Vault.

## Setup

1. source the alias keys
   ```
   source ../keys/swarm-alias.sh
   ```
1. setup the secrets: `make secrets`

   You will need to provide a personnel access token
   and github organization to use for authorization
1. build the local vault containers: `make build`
1. deploy vault : `make deploy`
1. check the status: `make status`

## Unseal the vault

It's possible to have a secure process with pgp for vault unseal, but
for now we'll use a simple exec process to get to vault and unseal it.

```
make unseal
```

You will be prompted to prepare to save the keys, then the keys will be used
to unseal the vault.

## Test

You can try testing read/write to vault with the following alias.
Make sure the docker engine you use is network accessible to the
swarm cluster.

```
export GITHUB_TOKEN=***-your-personel-access-token-****
func_vault() { docker run -it --rm \
    -e http_proxy -e https_proxy -e no_proxy \
    -e VAULT_ADDR=https://$docker_swarm_ip_swarm_node_1:8200 \
    -e VAULT_SKIP_VERIFY=1 --entrypoint sh vault \
    -c "vault auth -method=github token=$GITHUB_TOKEN && \
    vault $*" }
alias vault='func_vault '
```

Now you can execute these test commands:

```
vault write secret/hello value=world
vault read secret/hello
```

## Exercise

1. Using environment inputs to wait for consul and connect vault
   to consul

1. Create a secrets store for the vault bootstrap process


## Un-deploy

```
make undeploy
```

## Detailed unseal process

1. Use `make status` to get the node for where vault is running.
1. View the running process with the alias `docker-swarm-node-x ps` where
   `x` is the swarm node where vault is running, record the container id.
1. Use the container id to running the following command to connect to vault:
   ```
   docker-swarm-node-x exec -it <containerid> sh
   ```
1. While connected to the container initialize the vault unseal keys:
   ```
   vault init -key-shares=5 -key-threshold=2
   ```
1. Save the unseal keys and the Initialize Root Token for unseal operations.
   ```
    Unseal Key 1: key1xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    Unseal Key 2: key2xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    Unseal Key 3: key3xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    Unseal Key 4: key4xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    Unseal Key 5: key5xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    Initial Root Token: tokenxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   ```
1. Unseal the vault:
   ```
    vault unseal key1xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    vault unseal key2xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   ```
1. Authenticate with the initial root token
   ```
    vault auth -address=$VAULT_ADDR {initial_root_token}
   ```
1. Setup github auth with the following command:
   ```
   /usr/local/bin/set-github-auth.sh
   ```
