# 1global-creds.groovy

Provides a method to bootstrap credentials for jenkins operations
from vault, so that we can startup a secure connection with
GitHub at startup that doesn't store creds via env vars.
