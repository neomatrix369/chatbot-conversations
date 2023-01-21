#!/bin/bash

set -eu
set -o pipefail

INSTANCE_PUBLIC_IP="$(./get-instance-public-ip.sh)"

echo "Public IP address of the cloud instance running is ${INSTANCE_PUBLIC_IP}"

exit_code=0
#
#-t: Force pseudo-terminal allocation. This can be used to execute arbitrary
# screen-based programs on a remote machine, which can be very useful,
# e.g. when implementing menu services. Multiple -t options force tty
# allocation, even if ssh has no local tty.
#
ssh opc@${INSTANCE_PUBLIC_IP} \
	-i ${TF_VAR_private_key_path} \
    -t 'cd chatbot-conversations; ./docker-runner.sh --runContainer' \
    || exit_code=$? && true

if [[ ${exit_code} -eq 0 ]]; then
	echo ""
	echo "Exit code ${exit_code}."
	echo "Finished loading up the docker container in the cloud instance."
	echo ""
else
	if [[ ${exit_code} -eq 130 ]]; then
	    echo ""
		echo "Exit code ${exit_code}."
		echo "You terminated the running session via CTRL-C or CTRL-D. You can re-run this script by calling '$0' at the prompt again."
		echo ""
	else
		echo ""
		echo "Exit code ${exit_code}."
		echo "Failed trying to run the docker container, its possible it has already been executed, check the error logs above."
		echo "Or try to ssh into the container and investigate. Checkout README to find out debugging steps "
		echo "(hints: get-instance-public-ip.sh and ssh onto the remote VM)."
		echo ""
	fi
fi