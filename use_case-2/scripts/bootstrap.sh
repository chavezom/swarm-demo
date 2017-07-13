#!/bin/sh

# primary ENTRYPOINT script for vault
cat "${VAULT_CONFIG}.tpl" | sed "s/@CONSUL_SERVER@/$CONSUL_SERVER/g" > \
    "${VAULT_CONFIG}"
docker-entrypoint.sh server \
     -config=$VAULT_CONFIG -log-level=$LOG_LEVEL
