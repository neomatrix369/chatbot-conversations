# How to build and run the projects

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
cd roberta-world
./run-roberta-world.sh
```

_Fourth terminal window_
```bash
cd connecting_worlds
mvn clean package
```

Run this one only after the above three are up and running:

```bash
./run-connecting-worlds.sh
```

Watch the conversations across the fours windows. If they tend to go in a loop (happens rarely) repeating the same things again and again, then restart the `connecting world` app by re-running the shell-script.

All the conversations of the three worlds: Helidon, Quarkus and Roberta are amendable and can be customised.

## Eliza

Some Eliza related resources gathered during the preparation of this demo:
- Online version: http://manifestation.com/neurotoys/eliza.php3/
- Wikipedia: https://en.wikipedia.org/wiki/ELIZA
- Eliza code used in the demo: https://github.com/jthomason007/Eliza

## Roberta

Learn more about the [Roberta models](https://github.com/pytorch/fairseq/blob/master/examples/roberta/README.md#pre-trained-models) on the [Fairseq's gitHub repo](https://github.com/pytorch/fairseq/blob/master/examples/roberta/README.md).

For this demo we have make use of the Roberta base model but any of the avaiable models could have been used. Of course their API's may differ.

[Back to README.md](./README.md)