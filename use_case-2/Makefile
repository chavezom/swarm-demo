SHELL := /bin/bash
PREFIX ?= $(shell pwd)
ifneq ($(http_proxy),)
	BUILD_ARGS := $(BUILD_ARGS) --build-arg http_proxy=$(http_proxy)
endif
ifneq ($(https_proxy),)
	BUILD_ARGS := $(BUILD_ARGS) --build-arg https_proxy=$(http_proxy)
endif
ifneq ($(no_proxy),)
	BUILD_ARGS := $(BUILD_ARGS) --build-arg no_proxy=$(no_proxy)
endif

default: deploy

.PHONY: build clean status deploy undeploy unseal

secrets:
	$(PREFIX)/build/secrets.sh

status:
	$(PREFIX)/build/status.sh

build:
	BUILD_ARGS="$(BUILD_ARGS)" $(PREFIX)/build/build.sh

clean:
	$(PREFIX)/build/clean.sh

deploy: build
	$(PREFIX)/build/deploy.sh

undeploy:
	$(PREFIX)/build/undeploy.sh

unseal:
	$(PREFIX)/build/unseal.sh
