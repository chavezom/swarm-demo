# Introduction

This feature implements the 'yet-another-docker-plugin' used for docker/swarm/UCP (docker entreprise clustering).

It installs yet-another-docker-plugin in latest version.
UCP requires rc0.30


# Slaves available (currently)
Docker Image | Node Label | Repository URL | Notes
-------------|------------|----------------|------
hub.docker.hpecorp.net/dindc/jenkins-slave-dind:v1.13 | docker-v1.13 | [Docker v1.13 Slave](https://github.hpe.com/JenkinsHub/jenkins-slave-dind/tree/v1.13) | Docker Engine v1.13
hub.docker.hpecorp.net/dindc/jenkins-slave-dind:v17.06 | docker-v17.06 | [Docker v17.06 Slave](https://github.hpe.com/JenkinsHub/jenkins-slave-dind/tree/v17.06) | Docker Engine v17.06
hub.docker.hpecorp.net/dindc/hpe4it-ci-slave:master | docker | [hpe4it-jenkins-slave](https://github.hpe.com/JenkinsHub/hpe4it-jenkins-slave) | docker cli, docker-compose
hub.docker.hpecorp.net/larsonsh/jenkins-slave-base:0.1 | base | [jenkins-slave-base-images](https://github.hpe.com/JenkinsHub/jenkins-slave-base-images) | bash slave image
hub.docker.hpecorp.net/dindc/jenkins-slave-dind:master | docker-in-docker | [jenkins-slave-dind](https://github.hpe.com/JenkinsHub/jenkins-slave-dind) | docker engine, docker cli, docker-compose __Deprecated__ replace it with __docker-v1.13__

## Add your own slave image.
1. Get your docker slave image uploaded into [HPE's DTR](https://hub.docker.hpecorp.net/) or public [Docker Hub](https://hub.docker.com/), you can use the current slave images as templates or base.
1. Fork this repo and make the following changes:
  * Add your slave image configuration to the [yadp-config.json](yadp-config.json) file. [below](#configuration) is an example on how it should look like.
  * Add your slave image details in the above [table](#slaves-available-currently) of images available.
  * Submit a PR with your changes to this repo.

Done, have a :coffee: and be happy to save power and CO2 :evergreen_tree::deciduous_tree:
by leveraging shared resources!

## configuration

You need to create a file called `yadp-config.json` with the cloud and template configurations.

Exmaple:

```
[
  {
    "Cloud_Name": "ucp_stage",
    "Docker_URL": "tcp://dashboard.ucp-stage.g4ihos.itcs.hpecorp.net:443",
    "Docker_API_version": "",
    "Host_Credentials_vault_path": "secret/hpe4it-jenkins-ci/yad",
    "Connect_timeout": null,
    "Max_Containers": 50,
    "Images": [
      {
        "Labels": "docker",
        "Docker_Image_Name": "hub.docker.hpecorp.net/dindc/hpe4it-ci-slave:master",
        "Max_Instances": 10,
        "Timeout": 10,
        "Remote_Filing_System_Root": "/home/jenkins",
        "Linux_user": "jenkins",
        "Launch_timeout": 120,
        "Docker_Network_Mode": "default",
        "Jenkins_url": "default"
      },
      {
        "Labels": "docker_python",
        "Docker_Image_Name": "hub.docker.hpecorp.net/base/python:latest",
        "Max_Instances": 10,
        "Timeout": 10,
        "Remote_Filing_System_Root": "/home/jenkins",
        "Linux_user": "jenkins",
        "Launch_timeout": 120,
        "Docker_Network_Mode": "default",
        "Jenkins_url": "default"
      }
    ]
  },
  {
    "Cloud_Name": "ucp_dev",
    "Docker_URL": "tcp://dashboard.ucp-dev.g4ihos.itcs.hpecorp.net:443",
    "Docker_API_version": "",
    "Host_Credentials_vault_path": "secret/hpe4it-jenkins-ci-local/yad",
    "Connect_timeout": null,
    "Max_Containers": 50,
    "Images": [
      {
        "Labels": "vanilla",
        "Docker_Image_Name": "hub.docker.hpecorp.net/larsonsh/jenkins-slave-base:0.1",
        "Max_Instances": 10,
        "Timeout": 10,
        "Remote_Filing_System_Root": "/home/jenkins",
        "Linux_user": "jenkins",
        "Launch_timeout": 120,
        "Docker_Network_Mode": "default",
        "Jenkins_url": "default"
      }
    ]
  }
]
```
