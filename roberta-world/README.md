# Roberta World 

Roberta World is bot that assists by answering questions in a fun way, created to showcase the Roberta model. Roberta World has been customised to deliver a great learning experience.

# Installation

You will need:

- [Python](https://python.org/) 3.7 or higher
- Execute the steps in the [Usage](#usage) section 

# Usage

## Installing requirements (one-off)

```bash
pip install -r requirements.txt
```

## Run your app locally using the built-in web server

```
## running the chat server
$ FLASK_APP=flask-torch-app.py flask run -p 7070
```

or

```
## running the chat server in dev/debug mode
$ FLASK_ENV=development FLASK_APP=flask-torch-app.py flask run -p 7070
```

Runs the above ML server, in order to be able to send questions to the ML server and get an answer (if it can return one) with confidence scores. The server will stand up at [http://localhost:7070](http://localhost:7070) for REST API calls.

## Download RobertaModels

You can manually download Roberta models from [RoBERTa: A Robustly Optimized BERT Pretraining Approach](https://github.com/pytorch/fairseq/blob/master/examples/roberta/README.md#pre-trained-models) | [GitHub](https://github.com/pytorch/fairseq/blob/master/examples/roberta/README.md).

Pick one of them and ensure that the Flask app can find it by name.

Or use the shell script provided:

```bash
./download-roberta-model.sh    ## downloads roberta.base by default
```

or

```
./download-roberta-model.sh large
```

Although there is a chance the urls might change the shell script might not work.
