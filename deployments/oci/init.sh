#!/bin/bash

# Create a docker user and add the opc user to it
# This will allow us to call docker commands without sudo
groupadd docker
usermod -aG docker opc

# Install docker
if "$(uname -o)"=="GNU/Linux"; then
    echo 'Running on CentOS'
    yum install -y docker-engine
else 
    echo 'Running on Oracle Linux or another Linux'
    sudo dnf install -y dnf-utils zip unzip
    sudo dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo
    sudo dnf remove -y runc
    sudo dnf install -y docker-ce --nobest
fi

systemctl start docker
systemctl enable docker