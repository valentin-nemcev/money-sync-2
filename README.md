# Money sync

## Setup

Install Docker: [Windows](https://docs.docker.com/docker-for-windows/install/#download-docker-for-windows) or [Mac](https://docs.docker.com/docker-for-mac/install/#download-docker-for-mac)

## Runbook

Start application:
```bash
docker-compose up         # Start application
docker-compose --build up # Rebuild containers (required after configuration change) and start 
docker-compose down       # Stop application
```

Start clojure repl with app context:
```bash
docker-compose run app boot repl
```

Run several commands in the same app container:
```bash
docker-compose run app bash
```

Execute SQL queries:
```bash
docker-compose exec db psql -U postgres
```
