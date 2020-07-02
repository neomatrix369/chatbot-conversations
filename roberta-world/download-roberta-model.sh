#!/bin/bash

set -e
set -u
set -o pipefail

echo "Download Roberta Model... (default: base, if nothing specified in the CLI parameter)"
MODEL_TYPE=${1:-"base"}
MODELS_TARGET_FOLDER=models

mkdir -p models && echo "Creating the models folder to download and store models"

DOWNLOAD_FILENAME="roberta.${MODEL_TYPE}.tar.gz"
BASE_URL="https://dl.fbaipublicfiles.com/fairseq/models"
TARGET_FOLDER_NAME="${MODELS_TARGET_FOLDER}/$(basename ${DOWNLOAD_FILENAME%.*.*})"

if [[ -e "${TARGET_FOLDER_NAME}" ]]; then
    echo "${TARGET_FOLDER_NAME} already exists, skipping process. Delete the folder to create it again."
    exit -1
fi

echo "Downloading ${DOWNLOAD_FILENAME} from ${BASE_URL}"
if [[ ! -s "${MODELS_TARGET_FOLDER}/${DOWNLOAD_FILENAME}" ]]; then
    rm -fr "${MODELS_TARGET_FOLDER}/${TARGET_FOLDER_NAME}"
    curl -JLO "${BASE_URL}/${DOWNLOAD_FILENAME}"
    mv "${DOWNLOAD_FILENAME}" ${MODELS_TARGET_FOLDER}
else
    echo "${DOWNLOAD_FILENAME} already exists, proceeding with unzipping. Delete the file to download it again."
fi

tar -C ${MODELS_TARGET_FOLDER} -zxvf "${MODELS_TARGET_FOLDER}/${DOWNLOAD_FILENAME}"
