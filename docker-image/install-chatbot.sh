#!/bin/bash

#
# Copyright 2019, 2020, 2021 Mani Sarkar
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -e
set -u
set -o pipefail

source common.sh

gitClone https://github.com/neomatrix369/chatbot-conversations "add-examples-as-tutorials"
cd chatbot-conversations

echo "Downloading Maven wrapper artifacts"
curl -sL https://github.com/shyiko/mvnw/releases/download/0.1.0/mvnw.tar.gz | tar xvz

# Maven version can be changed with
(MAVEN_VERSION=3.6.3 &&
  sed -iEe "s/[0-9]\+[.][0-9]\+[.][0-9]\+/${MAVEN_VERSION}/g" .mvn/wrapper/maven-wrapper.properties)

echo "Building Chatbot using Maven"
set -x
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo "Skipping test failures - not ideal, but to gain some speed"
./mvnw install package -Dmaven.compiler.source=11 -Dmaven.compiler.target=11
       
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
set +x

CHATBOT_VERSION="0.1"

echo "Copying the necessary jars into the notebooks folder"

cp chatbot-conversations/target/chatbot-${CHATBOT_VERSION}.jar notebooks

cd ..