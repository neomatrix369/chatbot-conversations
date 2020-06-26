# Chatbot conversations

This project demonstrates a number of concepts:

- NLP (or a simulation of it)
- Chatbot interactions
- Communication between two apps via broker

## Components

- Helidon world
- Quarkus world
- Connecting worlds

## How to build and run the projects

Run each of them in a different terminal one after the other:

_First terminal window_
```bash
cd helidon-world
mvn clean package
./run-helidon-world.sh
```

_Second terminal window_
```bash
cd quarkus-world
mvn clean package
./run-quarkus-world.sh
```

_Third terminal window_
```bash
cd connecting_worlds
mvn clean package
```

Run this one only after the first two are up and running:

```bash
./run-connecting-worlds.sh
```

Watch the conversations across the three windows. If they tend to go in a loop, restart the `connecting world` app by re-running the shell-script.

All the conversations of the two worlds: Helidon and Quarkus are amendable and can be customised.