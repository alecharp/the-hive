TAG=$$(git rev-parse --short --verify HEAD)
IMAGE_NAME=$(USER)/the-hive

.PHONY: all build tag publish release

all: build

build:
	@[ -f target/the-hive.jar ] || mvn clean verify
	@docker build --compress -t $(IMAGE_NAME) -f src/main/docker/Dockerfile .

tag:
	@docker tag $(IMAGE_NAME) $(IMAGE_NAME):$(TAG)

publish: tag
	@docker push $(IMAGE_NAME):$(TAG)

release: build
	@make publish -e TAG=latest
