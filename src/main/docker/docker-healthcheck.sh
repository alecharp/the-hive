#!/bin/sh
set -euo pipefail

PORT=${PORT:-8080}
STATUS=$(curl -s -o /dev/null -w '%{http_code}' http://localhost:${PORT}/actuator/info || echo 1)
if [ ${STATUS} -eq 200 ]; then
  echo "Everything is awesome..."
  exit 0
else
  echo "This is not what are looking for..."
  exit 1
fi
