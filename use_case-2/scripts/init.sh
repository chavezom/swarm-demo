#!/bin/sh

# this script is a 1 time use script to initialize vault
# we'll echo the unseal keys

echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
echo "**** WARNING DO NOT SHARE SCREEN *******"
echo "**** THESE ARE YOUR KEYS TO SECURE *****"
echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
echo "press enter key to continue ..."
read -r c
keys=$(vault init -key-shares=5 -key-threshold=2)
echo "${keys}"
key1=$(echo "${keys}"| grep 'Unseal Key 1'|awk -F':' '{print $2}'|tr -d ' ')
key2=$(echo "${keys}"| grep 'Unseal Key 2'|awk -F':' '{print $2}'|tr -d ' ')
token=$(echo "${keys}"| grep 'Initial Root Token'|awk -F':' '{print $2}'|tr -d ' ')

eval "vault unseal ${key1}"
eval "vault unseal ${key2}"
eval "vault auth -address=$VAULT_ADDR ${token}"

/usr/local/bin/set-github-auth.sh

vault write secret/hello value=world
vault read secret/hello

vault auth -methods
echo "done"
