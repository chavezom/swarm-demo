backend "consul" {
  address = "@CONSUL_SERVER@:8500"
  path= "vault"
  scheme = "http"
}
listener "tcp" {
  address = "0.0.0.0:8200"
  tls_cert_file = "/run/secrets/vault_tls_cert"
  tls_key_file  = "/run/secrets/vault_tls_key"
}
disable_mlock = true
default_lease_ttl = "1h"
max_lease_ttl = "36h"
