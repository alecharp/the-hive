#!/bin/sh
set -euo pipefail

JAVA_OPTS="-XX:+UseG1GC
 -XX:+UseStringDeduplication
 -XX:+UnlockExperimentalVMOptions
 -XX:+UseCGroupMemoryLimitForHeap
 -Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true
"
if [[ $# -gt 0 ]]; then
    exec "$@"
else
    HERE="$(dirname $(readlink -f "$0"))"
    export MALLOC_ARENA_MAX=2
    exec "java" ${JAVA_OPTS} "-jar" "$HERE/the-hive.jar"
fi
