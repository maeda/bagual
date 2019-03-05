infrastructure:
	docker-compose up -d

run-local: infrastructure
	./gradlew bootRun
