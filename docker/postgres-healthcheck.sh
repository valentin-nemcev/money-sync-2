#!/bin/bash

set -xeo pipefail

args=(
  # force postgres to not use the local unix socket (test "external" connectibility)
  --host "$(hostname -i || echo '127.0.0.1')"
  --username postgres
  --quiet --no-align --tuples-only
)

if check="$(echo "SELECT 'healthcheck'" | psql "${args[@]}")" && [ "$check" = 'healthcheck' ]; then
  exit 0
fi

exit 1
