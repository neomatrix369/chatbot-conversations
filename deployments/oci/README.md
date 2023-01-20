# Deployment: Infrastructure as code

The scripts in this folder give the ability to provision and manage compute capacity using [Oracle Cloud Infrastructure](https://docs.cloud.oracle.com/iaas/Content/services.htm), in
order to deploy the docker container and run the notebook in it. 

In short the scripts does the below:
- create computes instances and associated 
network resources necessary to run an instance on OCI
- creates a virtual network with a subnet and puts one host on this subnet
- adds the necessary permissions to allow services to communicate in the subnet within specific ports (egress and ingress rules)
- runs the `init.sh` script to install basic tool(s) on the hosts 
- finally, the `provision.sh` script to prepare the instance ready to be used -- by triggering the docker container to stand up the Jupyter labs server
- also provides the necessary help in the form of ready-to-use shell-scripts toto perform various tasks with the help of `terraform` and `ssh`

**Table of content**
- [Pre-requisites](#pre-requisites)
- [Provisioning Infrastructure using Terraform](#provisioning-infrastructure-using-terraform)
  + [Setting up credentials](#setting-up-credentials)
  + [Create infrastructure from the CLI using Terraform](#create-infrastructure-from-the-cli-using-terraform)
  + [Deploy the docker image with the notebooks and libraries](#deploy-the-docker-image-with-the-notebooks-and-libraries)
  + [Recover/retry from failed attempt](#recoverretry-from-failed-attempt)
  + [Start clean after a failed attempt (errors encountered)](#start-clean-after-a-failed-attempt-errors-encountered)
  + [Destroy infrastructure (cleanup)](#destroy-infrastructure-cleanup)
- [Security](#security)
- [OCI and Terraform resources](#oci-and-terraform-resources)

## Pre-requisites

- **A [Oracle Cloud (OCI)](https://www.oracle.com/cloud/free/) Account (you maybe able to procure some FREE credits via the [Oracle Cloud Free Tier](https://www.oracle.com/cloud/free/) program)**
- A properly configured [Oracle Cloud Infrastructure account](https://docs.cloud.oracle.com/iaas/Content/API/Concepts/apisigningkey.htm). Please refer these blogposts to understand how you could do this manually: [1](https://medium.com/oracledevs/running-your-jupyter-notebooks-on-the-cloud-ed970326649f) | [2](https://medium.com/oracledevs/running-apache-zeppelin-on-oracle-cloud-infrastructure-b0aecc79597a) or see [Ensure OCI account is created and you can log in](https://docs.oracle.com/en-us/iaas/Content/General/Reference/PaaSprereqs.htm)
Note: the above step is one-off, you won't need to do this everything we run instances on the Oracle Cloud. But do ensure the minimum prerequisites are fulfilled.
- [Install Terraform](https://learn.hashicorp.com/terraform/getting-started/install.html) (all methods for the various platforms are mentioned)
- Clone this repo and in the right folder:
```bash
$ git clone https://github.com/neomatrix369/chatbot-conversations
$ cd chatbot-conversations
```
- Create your credentials file (one-off, you can reuse this for future purposes) in the `deployments/oci` folder. Use the `credentials.rc_template` to create your own credentials file for your OCI account. The file should look like the below (as per the `credentials.rc_template` file provided):

```bash
### Terraform env variables
export TF_VAR_tenancy_ocid=[TENANCY_OCID]
export TF_VAR_compartment_ocid=[COMPARMENT_OCID]
export TF_VAR_user_ocid=[USER_OCID]
export TF_VAR_fingerprint=[FINGERPRINT]
export TF_VAR_private_key_path=[PATH_TO_YOUR_ACCOUNT_PRIVATE_KEY eg: ~/.oci/key.pem]
export TF_VAR_region=[REGION NAME eg: uk-london-1]

### ssh keys that will be used for remote access authenication
export TF_VAR_ssh_public_key="$(cat [PATH_TO_SSH_PUBLIC_KEY])"

## We won't be assigning the private_key contents into an environment variable but pass it as an argument via the CLI
echo 'Pass -var "ssh_private_key=$(cat [PATH_TO_YOUR_SSH_PRIVATE_KEY])" when running the "terraform apply" or "terraform destroy" commands'
```
Note the `TF_VAR` prefix, as it is a terraform convention for input variables. 

Here is a list of resources on where to look for each of the above resources:

- [Refer to this link](https://cloud.oracle.com/tenancy)  to find out about `[TENANCY_OCID]` (look for the **OCID** field under the Tenancy Information on the page)
- Use the left-hand [Navigation menu > Identity > Compartments](https://cloud.oracle.com/identity/compartments), select the compartment to find out about `[COMPARMENT_OCID]` where you should look for the **OCID** field
- Go to `Profile Icon` > `User Settings` or left-hand [Navigation menu > Identity > Users](https://cloud.oracle.com/identity/users) > Select the user from the list (yourself), look for the **OCID** field to find out about `[USER_OCID]`
- Go to [Navigation menu > Identity > Users](https://cloud.oracle.com/identity/users) > Select the user from the list (yourself), then from under **Resource**, click on **API Keys > Add API Key**. Here you can choose to either generate a new key-pair or use an existing one (choose the Public key file or paste it's content), once done, you are shown the finger print for the `[TF_VAR_fingerprint]` variable
- And depending on the above choice, make sure you note the location of the Private key of the key-pair from the above selection, this is needed for `[TF_VAR_private_key_path]`
Other resources [1](https://docs.oracle.com/en-us/iaas/Content/API/Concepts/apisigningkey.htm#five) | [2](https://docs.oracle.com/en-us/iaas/Content/API/Concepts/apisigningkey.htm#two) | [3](https://docs.oracle.com/en-us/iaas/Content/API/Concepts/apisigningkey.htm#four) | [4](https://docs.oracle.com/en-us/iaas/Content/API/Concepts/apisigningkey.htm#five) (look for How to Upload the Public Key)
- Refer to [Regions page](https://cloud.oracle.com/regions/infrastructure) to look for the Region Identifier that is relevant to your account for the `[TF_VAR_region]` field
- `[TF_VAR_ssh_public_key]` and `[TF_VAR_ssh_private_key]` are already known to you, they are referring to the contents of the keys in `~/.ssh/id_rsa.pub` and `~/.ssh/id_rsa` files respectively.

For a summary (also helps to verify the steps) of the above steps please see [here](https://www.terraform.io/docs/providers/oci/index.html).

## Provisioning Infrastructure using Terraform

### Setting up credentials

- Source your credentials file

```bash
$ source credentials.rc
```

### Create infrastructure from the CLI using Terraform

- Deploy with terraform

```bash
$ terraform init
$ terraform apply -var "ssh_private_key=$(cat ~/.ssh/id_rsa)" --auto-approve
```

The deployment process should end with a list of private/public ip addresses like so:

```bash
Apply complete! Resources: 9 added, 0 changed, 0 destroyed.

Outputs:

instance_private_ips = [
    10.1.nn.m
]
instance_public_ips = [
    1xx.145.174.85
]

```

The public IP addresses are fairly dynamic in nature and could be between any range (example shown above). Please make a note of the Public IP above as it will be needed in the following steps.

### Deploy the docker image with the notebooks and libraries

- use `ssh` and `docker` to make that end meet

```bash
$ ./run-chatbot-conv-app-in-the-cloud.sh
```

#### Find out your public IP address of the currently running instance(s)

In order to know the public IP of the currently running instance(s) you build using terraform, please do the below:

```bash
$ ./get-instance-public-ip.sh
```

#### ssh onto the created instance(s)

In order log on to the running instance(s), please do the below:

```bash
$ ssh opc@[PUBLIC IP address]
```

Username `opc` is mandatory,  the `[PUBLIC IP address]` can be found using the `./get-instance-public-ip.sh` command.

As successful login, means you get the below prompt onto the VM instance, like the one below:

```bash
Last login: Thu May 26 18:46:45 2022 from 2.24.105.123
[opc@chatbot-oci-tf-host-public-0 ~]$ 
```

### Recover/retry from failed attempt

- Apply the fix to the configuration or script or both
- And run the below again:

```bash
$ terraform apply -var "ssh_private_key=$(cat ~/.ssh/id_rsa)" --auto-approve
```

### Start clean after a failed attempt (errors encountered)

- Run the below before proceeding:

```bash
$ terraform destroy -var "ssh_private_key=$(cat ~/.ssh/id_rsa)" --auto-approve
$ terraform apply -var "ssh_private_key=$(cat ~/.ssh/id_rsa)" --auto-approve
```

This is particularly important on OCI when one or more resources did not get created and before trying again we should clean-up or else Terraform may try to recreate an already existing (invalid instance) resource and you could get errors like "Service rate limit exceeded".

### Destroy infrastructure (cleanup)

- Remove resources or destroy them with terraform

```bash
$ terraform destroy -var "ssh_private_key=$(cat ~/.ssh/id_rsa)" --auto-approve
```

You should see something like this at the end of a successful run:

```text
.
.
.
Destroy complete! Resources: 7 destroyed.
```


### Security

Please beware when using this in your target domain depending on the prerequisites you need to conform to. This example is good for learning and illustration purposes, please do NOT deploy it in production or public facing environments without taking into consideration security and other requirements.

### Setup specific region
- Go to https://cloud.oracle.com/compute/instances?region=[region] (eg.: region=sa-saopaulo-1) to find the name of the VM shape (for example: VM.Standard.E2.1.Micro) and instance image name (Image: Oracle Linux 8 and Image build: 2022.12.15-0) 
- Add region OCID to the infrastructure.tf
```bash
variable "instance_image_ocid" {
  type = map(string)

  default = {

    # ...

    sa-saopaulo-1  = "ocid1.image.oc1.sa-saopaulo-1.aaaaaaaa3ibxbkfvmcdyshvkuzhpc2wx2ofmpjyyjf5tyh3eqge7vc7d5rtq"
  }
}
```

### Override default VM shape
- Go to the all images page (https://docs.oracle.com/en-us/iaas/images/) and use the image name and build to search proper region related image OCID (for example: Oracle-Linux-8.6-2022.12.15-0) and take the OCID for sa-saopaulo-1 (ocid1.image.oc1.sa-saopaulo-1.aaaaaaaa3ibxbkfvmcdyshvkuzhpc2wx2ofmpjyyjf5tyh3eqge7vc7d5rtq)
- Add instance shape variable to your credentials.rc
```bash
export TF_VAR_instance_shape="VM.Standard.E2.1.Micro"
```

### Use specific init script
`init.sh` script was written to install and setup docker on the hosts for default image.
If you change the image, you might need to write and run your own script.
- Create your own init script to install docker on the host (eg: init.oracle8.sh)
- Add init path variable to your credentials.rc
```bash
export TF_VAR_init_path="./init.oracle8.sh"
```

### OCI and Terraform resources

- For more information on Terraform infrastructure management see: [Terraform for OCI](https://www.terraform.io/docs/providers/oci/index.html)
- [Terraform commands](https://www.terraform.io/docs/commands/index.html)
- [See working example using Tribuo running on GraalVM](https://github.com/neomatrix369/awesome-ai-ml-dl/tree/master/examples/tribuo/deployments/oci)
- [Ensure OCI account is created and you can log in](https://docs.oracle.com/en-us/iaas/Content/General/Reference/PaaSprereqs.htm) 
- Install OCI cli command tool (or use a language specific SDK) -- one-off task]
  - https://docs.oracle.com/en-us/iaas/Content/API/Concepts/sdks.htm#Software_Development_Kits_and_Command_Line_Interface
    - [Download CLI](https://docs.oracle.com/en-us/iaas/Content/API/SDKDocs/cliinstall.htm#Quickstart)
    + Setting up the Config File 
        + [Where to Get the Tenancy's OCID and User's OCID](https://docs.oracle.com/en-us/iaas/Content/API/Concepts/apisigningkey.htm#five)
        + [Regions and Availability Domains](https://docs.oracle.com/en-us/iaas/Content/General/Concepts/regions.htm#top)
        + [SDK and CLI configuratuon File](https://docs.oracle.com/en-us/iaas/Content/API/Concepts/sdkconfig.htm)
- How to find values of the Terraform OCI related variables
    - [Oracle Cloud Infrastructure account](https://docs.cloud.oracle.com/iaas/Content/API/Concepts/apisigningkey.htm)
- [What are Compartments?](https://cloud.oracle.com/identity/compartments)

---

Go to [Main page](../../README.md)