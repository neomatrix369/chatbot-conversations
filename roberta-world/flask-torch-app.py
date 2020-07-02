mport random as rnd
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
    model_type = _model_type
    if model:
        return f'Model {BLUE}{model_name} ({model_type}){ANSI_RESET} already loaded.'
    else:
        print(f'Model {BLUE}{model_name} ({model_type}){ANSI_RESET} not loaded, loading now...')
        if model_type is None:
            model_type = "base"
        PATH = f'./models/roberta.{model_type}'
        model = RobertaModel.from_pretrained(PATH)
        eval_model()
        print(f'Model {BLUE}{model_name} ({model_type}){ANSI_RESET} loaded and evaluated.')
        return model


def eval_model():
    global model, model_type
    if model:
        print("Model loaded, evaluating now.")
        model.eval()
        print(f'Finished evaluating Model {BLUE}{model_name} ({model_type}){ANSI_RESET}.')
    else:
        print(f'Model {BLUE}{model_name} ({model_type}){ANSI_RESET} not loaded.')


@app.before_first_request
def on_startup():
    global model
    print("This is the first request hence loading the model")
    print("---")
    print(f"{YELLOW}{computer_name}{ANSI_RESET} will either repeat the same message the Other world sends.")
    print("Or will change a word or two in a sentence when replying. Rarely will a different sentence be sent out.")
    print("In this process new sentences can be produced but sometimes erroneous ones as well, watch out!")
    print(f"As you can see, {YELLOW}{computer_name}{ANSI_RESET} can guess the whole sentence even if you hide/mask a word in it.")
    print("---")

    model = load_model("base")   ### option params: large, large.mnli or large.wsc
    ## See https://github.com/pytorch/fairseq/blob/master/examples/roberta/README.md#pre-trained-models
    ## for more details about the above model types.


@app.route('/status')
def status() 
    return 'OK!'


def shuffle_words(message):
    split_sentence = message.split()
    rnd.shuffle(split_sentence)
    return " ".join(split_sentence)


def drop_words(message, exclude_word):
    tokenised_message = message.split()
    number_of_words = len(tokenised_message)

    if number_of_words > 3:
        try_again = True
        random_word = ""

        drop_n_words = 1
        if number_of_words > 10:
            drop_n_words = number_of_words // 5

        drop_words_counter = 0
        while drop_words_counter < drop_n_words:
            while try_again:
                randon_word_index = round(rnd.random() * number_of_words) - 1
                random_word = tokenised_message[randon_word_index]
                try_again = (random_word in [exclude_word, '-', '--', '.', '?'])
            drop_words_counter += 1

            message = message.replace(random_word, '')
        return message.replace('  ', ' ')

    return message


def add_or_insert_mask_to(message):
    if MASK_TOKEN in message:
        return message

    tokenised_message = message.split()
    number_of_words = len(tokenised_message)

    if number_of_words > 2:
        randon_word_index = round(rnd.random() * number_of_words) - 1
        random_word = tokenised_message[randon_word_index]
        return drop_words(message, random_word).replace(random_word, MASK_TOKEN)

    return message.strip() + f' {MASK_TOKEN}'


def get_any_answer_from(response):
    random_answer_index = round(rnd.random() * len(response)) - 1
    return response[random_answer_index][0], response[random_answer_index][1]


@app.route('/send')
def send():
    global model_type
    if len(request.args) == 0:
        return f'Flask app serving {model_name} ({model_type})! Please pass the message parameter.'

    message = request.args.get('message')

    print()
    print(f"{YELLOW}Other world{ANSI_RESET}: {message}")

    masked_message = add_or_insert_mask_to(message)

    confidence_score = 1.00
    try:
        same_answer_try_again = True
        while same_answer_try_again:
            response = model.fill_mask(masked_message, topk=3)
            if response:
                response, confidence_score = get_any_answer_from(response)
                response = response.strip()
            else:
                response = "<No response(s) returned>"
            same_answer_try_again = message == response

        if message == response:
            print(f"{RED}masked_message{ANSI_RESET}:", masked_message)
    except Exception as ex:
        print(f"Error parsing this sentence '{masked_message}', due to '{str(ex)}'")
        response = "<error> I'm having internal problems, when trying to work out what to say."

    print(f"{YELLOW}{computer_name}{ANSI_RESET}: {response} [confidence: {round(confidence_score * 100, 3)}%]")
    print()
    
    return response

