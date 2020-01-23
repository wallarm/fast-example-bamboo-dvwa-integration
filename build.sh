#!/bin/sh
touch env_file.env
env | grep ^bamboo_ > env_file.env
