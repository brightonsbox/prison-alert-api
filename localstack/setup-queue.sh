#!/usr/bin/env bash

export AWS_ACCESS_KEY_ID=anykey
export AWS_SECRET_ACCESS_KEY=anysecret
export AWS_DEFAULT_REGION=eu-west-2

HOSTNAME=${EXTERNAL_HOSTNAME:-"localhost"}

echo 'Setting up localstack SQS...'
aws --endpoint-url=http://${HOSTNAME}:4576 sqs create-queue --queue-name prison-alerts 
echo 'Finished localstack setup'
exit 0
