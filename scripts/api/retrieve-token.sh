#!/bin/bash

TOKEN=$(curl -v -X POST "localhost:8081/auth/oauth/token?grant_type=client_credentials" -H 'Content-Type: application/json' -H 'Content-Length: 0' -H "Authorization: Basic $(echo -n omicadmin:clientsecret| base64)" | jq -r '.access_token')

echo $TOKEN
