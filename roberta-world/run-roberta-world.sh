#!/bin/bash

set -e
set -u
set -o pipefail

FLASK_APP=flask-torch-app.py flask run -p 7070
