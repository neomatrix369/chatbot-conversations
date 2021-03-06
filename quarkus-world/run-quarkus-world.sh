#!/bin/bash

set -e
set -u
set -o pipefail

FILES_CHANGED=$(git diff --name-only || true)
if [[ ! -z "${FILES_CHANGED}" ]]; then
    mvn package &> ${TMPDIR:-/tmp}/run.logs
fi

clear
java -jar target/quarkus-world-1.0-SNAPSHOT-runner.jar
