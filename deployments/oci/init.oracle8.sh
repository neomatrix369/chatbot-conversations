#!/bin/bash

# Create a docker user and add the opc user to it
# This will allow us to call docker commands without sudo
groupadd docker
usermod -aG docker opc

# Install docker
dnf install -y dnf-utils zip unzip
dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo
dnf remove -y runc
dnf install -y docker-ce --nobest

systemctl start docker
systemctl enable docker