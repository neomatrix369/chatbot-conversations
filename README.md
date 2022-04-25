# Chatbot conversations

This project demonstrates a number of concepts:

- NLP (or a simulation of it, using [Eliza](#Eliza))
- NLP (or a simulation of it, using [Roberta](#Roberta))
- Chatbot interactions (using REST API)
- Microservices frameworks (Helidon and Quarkus)
- And also using a simple `python` Flask app 
- Communication between three apps via a central app
- Flexibility to add more chatbots
- Ability to change the models used in place of the existing ones

## Components

- Helidon world (Java)
- Quarkus world (Java)
- Roberta world (Python)
- Connecting worlds (Java)

---
## Getting Started

### Clone repository
```
git clone https://github.com/neomatrix369/chatbot-conversations.git

cd chatbot-conversations
```

### Run docker container
```
./docker-runner.sh --runContainer
```

--- 
_**[Tweet: Making chatbots have a conversation video](https://twitter.com/theNeomatrix369/status/1287293868376039424)**_

[![video: helidon-world|quarkus-world|connecting-worlds](https://user-images.githubusercontent.com/1570917/88921265-20fa6780-d266-11ea-8e32-9debd9dc5710.png)](https://www.youtube.com/watch?v=2daclN-yAfI&feature=youtu.be&t=2747 "Video: Chatbots talking to each other in action")

--- 

See the different worlds (chat components) in conversation with each other:

![helidon-world|quarkus-world|connecting-worlds](https://user-images.githubusercontent.com/1570917/87869702-9bd79e80-c999-11ea-86d0-3cfd16aa1d84.png)
![helidon-world](https://user-images.githubusercontent.com/1570917/87869761-0ab4f780-c99a-11ea-9a36-b72e09dd63d5.png)
![roberta-world](https://user-images.githubusercontent.com/1570917/87869763-0dafe800-c99a-11ea-836a-ccf269887997.png)
![connecting-worlds: helidon and roberta](https://user-images.githubusercontent.com/1570917/87869767-10aad880-c99a-11ea-919b-283b23b043c8.png)

## How to build and run the projects

See how you can make the different worlds chat to each other, go to [How to build and run the projects](./how-to-build-and-run-the-projects.md).

You can also find out the underlying components that make up the NLP aspect of the chatbots there.

## Run the Chatbot docker container

### Build the docker container

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

### Run the docker container to the command prompt


```bash
$ ./docker-runner.sh --runContainer

or

$ ./docker-runner.sh --dockerUserName "your_docker_username" --runContainer
```

## Credits

Credits to the original authors of the code (different projects) used in this project.

## Disclaimer

This a demo created for illustration purposes, please don't expect a fully functional app or high quality code.
The focus has been to use simple ready-to-use components and illustrate the original ideas via a new app.