#!/bin/bash

. retrieve-token.sh
curl -X GET 'http://localhost:8082/api/bookings/-1/alerts' -H "Authorization: Bearer $TOKEN" | jq
