IMAGE_FILE?=image.yaml
COMMIT?=latest
IMAGE_VERSION?=latest
DOCKERFILE_DIR?=build
DOGEN_VERSION?=2.0.0rc12

all:
	sh build.sh
	docker run -i --rm -v ${CURDIR}:/tmp/output:z jboss/dogen:$(DOGEN_VERSION) --verbos /tmp/output/$(IMAGE_FILE) /tmp/output/build
	docker build -t $(REPO):$(IMAGE_VERSION) build
