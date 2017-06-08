IMAGE_FILE?=image.yaml
COMMIT?=latest
IMAGE_VERSION?=latest
DOCKERFILE_DIR?=build
REPO?=$(shell cat $(IMAGE_FILE) | grep "^name:" | cut -d' ' -f2)
DOGEN_VERSION?=2.0.0

all: 
	mvn clean install -DskipTests
	echo "Running docker build $(REPO)"
	docker run -i --rm -v ${CURDIR}:/tmp/output:z -v ${CURDIR}/../repos/7.3/jboss-epel.repo:/dogen/additional_scripts/jboss-epel.repo:z -v ${CURDIR}/../repos/7.3/jboss-rhel-os.repo:/dogen/additional_scripts/jboss-rhel-os.repo:z -v ${CURDIR}/scripts:/tmp/scripts:z jboss/dogen:$(DOGEN_VERSION) --verbos /tmp/output/$(IMAGE_FILE) --scripts /tmp/scripts --repo-files-dir /dogen/additional_scripts/ /tmp/output/build
	docker build -t $(REPO):$(IMAGE_VERSION)  build
  
## BEFORE Merging into master, update docker-build.sh to not pass version as an arg (assuming all docker files are being built by this repo
#	curl -s https://raw.githubusercontent.com/EnMasseProject/travis-scripts/master/docker-build.sh | bash /dev/stdin $(REPO) $(DOCKERFILE_DIR)

clean:
	rm -rf build
