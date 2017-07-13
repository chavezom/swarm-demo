FROM jenkins:2.60.1-alpine

USER root
# Install build tools and docker
RUN apk add --no-cache \
    build-base \
    curl \
    dumb-init \
    openssl && \
    apk add --update \
        --repository http://dl-cdn.alpinelinux.org/alpine/edge/community/ \
        tini docker && \
    mkdir -p /git/seed-jobs && \
    chown -R jenkins:jenkins /git

ARG VAULT_VERSION=0.6.0
RUN curl -L "https://releases.hashicorp.com/vault/${VAULT_VERSION}/vault_${VAULT_VERSION}_linux_amd64.zip" -o /tmp/vault.zip && \
    unzip /tmp/vault.zip -d /usr/local/bin && \
    rm -f /tmp/vault.zip

USER jenkins

COPY config/plugins.txt /usr/share/jenkins/ref/plugins.txt

ENV INIT_GROOVY_D=/usr/share/jenkins/ref/init.groovy.d/
ENV JENKINS_EXECUTORS=3

COPY config/proxy/proxy.* $INIT_GROOVY_D
COPY config/executors/executors.* $INIT_GROOVY_D
COPY config/global-creds/global-creds.* $INIT_GROOVY_D
COPY config/github-authentication/github-authentication.* $INIT_GROOVY_D
COPY config/matrix-authorization/matrix-authorization.* $INIT_GROOVY_D
COPY config/github-branch-pipeline/github-branch-pipeline.* $INIT_GROOVY_D
COPY config/seed-job/seed-job.* $INIT_GROOVY_D
COPY config/jnlp-config/jnlp-config.* $INIT_GROOVY_D
COPY config/yadp/yadp.* $INIT_GROOVY_D
COPY config/load_yadp/load_yadp.* $INIT_GROOVY_D

# COPY lib $JENKINS_HOME/init.groovy.d/lib
COPY lib $INIT_GROOVY_D/lib

# Configure the yet-another-docker-plugin
ENV YADP_CONFIG_JSON=/config/yadp-config.json
COPY config/yadp-config.json /config/yadp-config.json

RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# setup the seed-jobs repo locally as a default
# this can be overriden by providing SEED_JOBS_REPO
# with a real https:// repo url
ENV SEED_JOBS_REPO=/git/seed-jobs
COPY jobs /git/seed-jobs
RUN cd /git/seed-jobs && \
    git config --global user.email "nouser@noreply.com" && \
    git config --global user.name "nouser" && \
    git init && \
    git add -A && \
    git commit -m "container based seed-jobs repo for init"



#
# EXPOSE 8080 50000
