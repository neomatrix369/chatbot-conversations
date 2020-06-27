# Chatbot conversations

This project demonstrates a number of concepts:

- NLP (or a simulation of it, using [Eliza](#Eliza))
- Chatbot interactions (using REST API)
- Microservices frameworks (Helidon and Quarkus) 
- Communication between two apps via a central app

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

## Eliza

Some Elize related resources gathered during prepartion of this demo:
- Online version: http://manifestation.com/neurotoys/eliza.php3/
- Wikipedia: https://en.wikipedia.org/wiki/ELIZA
- Eliza code used in the demo: https://github.com/jthomason007/Eliza

## Credits

Credits to the original authors of the code (different projects) used in this project.


## Disclaimer

This a demo created for illustration purposes, please don't expect a fully functional app or high quality code.
The focus has been to use simple ready-to-use components and illustrate the original ideas via a new app.  