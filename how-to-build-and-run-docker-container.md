# How to build and run the Chatbot docker container

## Build the docker container

Ensure your environment has the below variable set, or set it in your `.bashrc` or `.bash_profile` or the relevant startup script:

```bash
export DOCKER_USER_NAME="your_docker_username"
```

You must have an account on Docker hub under the above user name.

```bash
$ ./docker-runner.sh --buildImage

or

$ ./docker-runner.sh --dockerUserName "your_docker_username" --buildImage 
```

## Run the docker container to the command prompt

```bash
$ ./docker-runner.sh --runContainer

or

$ ./docker-runner.sh --dockerUserName "your_docker_username" --runContainer
```

[Back to the README.md page](./README.md)