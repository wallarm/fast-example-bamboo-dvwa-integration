#!/bin/sh

docker-compose build -e WALLARM_API_TOKEN=${bamboo_secret_token}
