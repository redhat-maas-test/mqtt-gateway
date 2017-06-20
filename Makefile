IMAGE_FILE?=image.yaml
COMMIT?=latest
IMAGE_VERSION?=latest
DOCKERFILE_DIR?=build
REPO?=$(shell cat $(IMAGE_FILE) | grep "^name:" | cut -d' ' -f2)
DOGEN_VERSION?=2.0.1
DOCKER_BUILD_OPTS?=""
DOCKER?=docker

all: 
	env
	mkdir -p build/
	cp -r target/mqtt-gateway-1.0-SNAPSHOT-bin.tar.gz build/
	echo "Running docker build $(REPO)"
	$(DOCKER) run -i --rm -v ${CURDIR}:/tmp/output:z -v ${CURDIR}/../repos/7.3/jboss-epel.repo:/dogen/additional_scripts/jboss-epel.repo:z -v ${CURDIR}/../repos/7.3/jboss-rhel-os.repo:/dogen/additional_scripts/jboss-rhel-os.repo:z -v ${CURDIR}/scripts:/tmp/scripts:z jboss/dogen:$(DOGEN_VERSION) --verbos /tmp/output/$(IMAGE_FILE) --scripts /tmp/scripts --repo-files-dir /dogen/additional_scripts/ /tmp/output/build
	$(DOCKER) build $(DOCKER_BUILD_OPTS) -t $(REPO):$(IMAGE_VERSION) build

clean:
	rm -rf build
