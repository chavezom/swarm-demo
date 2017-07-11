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
        tini docker

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

RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt


#
# EXPOSE 8080 50000
