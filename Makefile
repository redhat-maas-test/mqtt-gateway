IMAGE_FILE?=image.yaml
COMMIT?=$(shell git rev-parse HEAD | cut -c1-8)
IMAGE_VERSION?=latest
DOCKERFILE_DIR?=build
REPO?=$(shell cat $(IMAGE_FILE) | grep "^name:" | cut -d' ' -f2)
DOCKER_BUILD_OPTS?=""
DOCKER?=docker

build:
	mkdir -p $(CURDIR)/build
	cp -r target/mqtt-gateway-1.0-SNAPSHOT-bin.tar.gz $(CURDIR)/build/

	echo "Running docker build $(REPO)"
	dogen --verbose --scripts $(CURDIR)/scripts $(IMAGE_FILE) $(CURDIR)/build
	$(DOCKER) build $(DOCKER_BUILD_OPTS) -t $(REPO):$(COMMIT) $(CURDIR)/build

push:
	$(DOCKER) tag $(REPO):$(COMMIT) $(DOCKER_REGISTRY)/$(REPO):$(COMMIT)
	$(DOCKER) push $(DOCKER_REGISTRY)/$(REPO):$(COMMIT)

snapshot:
	$(DOCKER) tag $(REPO):$(COMMIT) $(DOCKER_REGISTRY)/$(REPO):$(IMAGE_VERSION)
	$(DOCKER) push $(DOCKER_REGISTRY)/$(REPO):$(IMAGE_VERSION)

clean:
	rm -rf build target
