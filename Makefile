infrastructure:
	docker-compose up -d

run-local: infrastructure
	./gradlew bootRun

test:
	./gradlew test

integration-test:
	./gradlew integrationTest