#!/bin/bash

MESSAGE_FILENAME="alert-inserted.json"
TRIMMED_MESSAGE=$(cat $MESSAGE_FILENAME | jq | tr -d "\n[:space:]" )

if ! [ -z "$TRIMMED_MESSAGE" ]; then
  aws --endpoint-url=http://localhost:4576 sqs send-message --queue-url http://localhost:4576/000000000000/prison-alerts --message-body $TRIMMED_MESSAGE --region eu-west-2 
else
  echo "Message parsing failed"
fi
