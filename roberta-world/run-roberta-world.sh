#!/bin/bash

set -e
set -u
set -o pipefail

echo "Before running Roberta let's download the necessary model."
./download-roberta-model.sh || true

echo "Running Roberta now that the necessary model is available."
FLASK_APP=flask-torch-app.py flask run -p 7070
