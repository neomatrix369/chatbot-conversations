import random as rnd
from flask import Flask
from flask import request

app = Flask(__name__)

from fairseq.models.roberta import RobertaModel


## https://www.lihaoyi.com/post/BuildyourownCommandLinewithANSIescapecodes.html
RED = "\u001b[31m"     
GREEN = "\u001b[32m"
BLUE = "\u001b[34m"
YELLOW = "\u001b[33m"
ANSI_RESET = "\u001b[0m"

MASK_TOKEN="<mask>"
model_name = "Roberta"
model = None
model_type = None
computer_name = model_name


def load_model(_model_type="base"):
    global model, model_type
    model_type=_model_type
    if model:
        return f'Model {model_name} ({model_type}) already loaded.'
    else:
        print(f'Model {model_name} ({model_type}) not loaded, loading now...')
        if model_type is None:
            model_type = "base"
        PATH = f'./models/roberta.{model_type}'
        model = RobertaModel.from_pretrained(PATH)
        eval_model()
        print(f'Model {model_name} ({model_type}) loaded and evaluated.')
        return model


def eval_model():
    global model, model_type
    if model:
        print("Model loaded, evaluating now")
        model.eval()
        print(f'Finished evaluating Model {model_name} ({model_type}).')
    else:
        print(f'Model {model_name} ({model_type}) not loaded.')


@app.before_first_request
def on_startup():
    global model
    print("This is the first request hence loading the model")
    print("---")
    print("{computer_name} will either repeat the same message the other person sends.")
    print("Or will change a word or two in a sentence when replying. Rarely will a different sentence be sent out.")
    print("As you can see, {computer_name} can guess the whole sentence even if you hide/mask a word in it.")
    print("---")

    model = load_model("base")


@app.route('/status')
def status():
    return 'OK!'


def shuffle_words(message):
    split_sentence = message.split()
    rnd.shuffle(split_sentence)
    return " ".join(split_sentence)


def add_or_insert_mask_to(message):
    if MASK_TOKEN in message:
        return message

    tokenised_message = message.split()
    number_of_words = len(tokenised_message)

    if number_of_words > 2:
        randon_word_index = round(rnd.random() * number_of_words) - 1
        random_word = tokenised_message[randon_word_index]
        return shuffle_words(message.replace(random_word, MASK_TOKEN))

    return message.strip() + f' {MASK_TOKEN}'


@app.route('/send')
def send():
    global model_type
    if len(request.args) == 0:
        return f'Flask app serving {model_name} ({model_type})! Please pass the message parameter.'

    message = request.args.get('message')

    print()
    print(f"{YELLOW}Other Person:{ANSI_RESET}: {message}")

    masked_message = add_or_insert_mask_to(message)

    response = model.fill_mask(masked_message, topk=3)
    if response:
        response = response[0][0]
    else:
        response = "<No response(s) returned>"

    print(f"{YELLOW}{computer_name}{ANSI_RESET}: {response}")
    print()
    
    return response

