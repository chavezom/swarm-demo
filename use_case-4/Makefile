SHELL := /bin/bash
PREFIX ?= $(shell pwd)

default: deploy

.PHONY: build clean status deploy undeploy

secrets:
	$(PREFIX)/build/secrets.sh

status:
	$(PREFIX)/build/status.sh

build:
	$(PREFIX)/build/build.sh

clean:
	$(PREFIX)/build/clean.sh

deploy: build
	$(PREFIX)/build/deploy.sh

undeploy:
	$(PREFIX)/build/undeploy.sh
