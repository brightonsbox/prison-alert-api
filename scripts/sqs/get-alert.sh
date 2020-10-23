#!/bin/bash

RESPONSE=$(aws --endpoint-url=http://localhost:4576 sqs receive-message --queue-url http://localhost:4576/000000000000/prison-alerts)
RECEIPT_HANDLE=$(echo $RESPONSE | jq -r '.Messages[0].ReceiptHandle')

echo $RESPONSE | jq

if ! [[ -z "$RECEIPT_HANDLE" ]]; then
  aws --endpoint-url=http://localhost:4576 sqs delete-message --queue-url http://localhost:4576/000000000000/prison-alerts --receipt-handle $RECEIPT_HANDLE
fi

