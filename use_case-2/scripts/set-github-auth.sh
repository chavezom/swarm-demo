#!/bin/sh

vault auth-enable github
vault write auth/github/config \
     organization=$(cat /run/secrets/VAULT_GITHUB_ADMIN_ORG) \
    base_url="${GITHUB_API_URL}" max_ttl=1h

# setup policies
vault policy-write default /config/default.hcl
vault write auth/github/map/teams/$(cat /run/secrets/VAULT_GITHUB_ADMIN_ORG) value=default
vault auth -methods
