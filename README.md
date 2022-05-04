# Chatbot conversations [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

![](https://img.shields.io/badge/java-8_|_11-blue) || ![](https://img.shields.io/badge/powered%20by-GraalVM-brightgreen)
![](https://img.shields.io/badge/powered%20by-Helidon-lightblue)
![](https://img.shields.io/badge/powered%20by-Quarkus-salmon)
||
[![Python versions](https://img.shields.io/pypi/pyversions/nlp_profiler.svg)](https://pypi.org/project/nlp_profiler/) 
![](https://img.shields.io/badge/powered%20by-Flask-purple)
![AI Models](https://img.shields.io/badge/powered%20by%20AI%20Models%20(NLP)-Eliza%20|%20RoBERTa-lightgrey)
||
[![Chatbot](https://img.shields.io/docker/pulls/neomatrix369/chatbot.svg)](https://hub.docker.com/r/neomatrix369/chatbot)
[![Chatbot](https://img.shields.io/docker/pulls/yugoccp/chatbot.svg)](https://hub.docker.com/r/yugoccp/chatbot)
||
[![Codeac](https://static.codeac.io/badges/2-471206886.svg "Codeac")](https://app.codeac.io/github/yugoccp/chatbot-conversations)

This project demonstrates a number of concepts:

- NLP (or a simulation of it, using [Eliza](#Eliza))
- NLP (or a simulation of it, using [Roberta](#Roberta))
- Chatbot interactions (using REST API)
- Microservices frameworks (Helidon and Quarkus)
- And also using a simple `python` Flask app 
- Communication between three apps via a central app
- Flexibility to add more chatbots
- Ability to change the models used in place of the existing ones

## Getting Started

### Clone repository
```bash
git clone https://github.com/neomatrix369/chatbot-conversations.git

cd chatbot-conversations
```

### Run docker container
```bash
./docker-runner.sh --runContainer
```
## Components

- Helidon world (Java)
- Quarkus world (Java)
- Roberta world (Python)
- Connecting worlds (Java)
  
## Presentations and Social media coverage

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

## How to build and run the Chatbot docker container

See how you can build and run your own docker container in your machine, go to [How to build and run docker container](./how-to-build-and-run-docker-container.md).


## Credits

Credits to the original authors of the code (different projects) used in this project.
Special thanks tp [@yugoccp](https://github.com/yugoccp), [@brjavaman](https://github.com/brjavaman) and [code4.life](https://code4.life) for giving us support for this community initiative!

## Disclaimer

This a demo created for illustration purposes, please don't expect a fully functional app or high quality code.
The focus has been to use simple ready-to-use components and illustrate the original ideas via a new app.
