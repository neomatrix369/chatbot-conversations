#!/bin/bash

sudo yum install -y git

# Open ports for the services to communicate
sudo firewall-cmd --zone=public --permanent --add-port=8080-8085/tcp
sudo firewall-cmd --zone=public --permanent --add-port=8443/tcp
sudo firewall-cmd --reload

git clone https://github.com/neomatrix369/chatbot-conversations
cd chatbot-conversations
./docker-runner.sh --pullImageFromHub

echo "|-----------------------------------------------------------------------------------------------------|"
echo "|                                                                                                     |"
echo "| Run the below command to start the chatbot conversations:                                           |"
echo "|                                                                                                     |"
echo "|       $ ./run-chatbot-conv-app-in-the-cloud.sh                                                      |"
echo "|                                                                                                     |"
echo "| Or you can ssh onto the VM instance by doing this:                                                  |"
echo "|                                                                                                     |"
echo "|-----------------------------------------------------------------------------------------------------|"