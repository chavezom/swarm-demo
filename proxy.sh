#!/bin/bash

# setup the proxy
#
echo "Proxy -> $PROXY"

if [ ! -z "$PROXY" ] &&  [ ! "$PROXY" = '"nil"' ]; then
    echo "setting up proxy"
    export http_proxy=http://$PROXY
    export https_proxy=https://$PROXY
    export HTTP_PROXY=$http_proxy
    export HTTPS_PROXY=$https_proxy
    export ftp_proxy=$(echo $http_proxy | sed 's/^http/ftp/g')
    export socks_proxy=$(echo $http_proxy | sed 's/^http/socks/g')
    export no_proxy="/var/run/docker.sock,localaddress,localhost,hpe.com,hpecorp.net,127.0.0.1"
    
    for i in $(seq 100 120); do
	    export no_proxy="${no_proxy},192.168.99.${i}"
    done
    export NO_PROXY=$no_proxy


else

    unset PROXY
    unset http_proxy
    unset https_proxy
    unset HTTP_PROXY
    unset HTTPS_PROXY
    unset ftp_proxy
    unset socks_proxy
    unset no_proxy
    unset NO_PROXY

    echo "unsetting proxy settings"
fi
