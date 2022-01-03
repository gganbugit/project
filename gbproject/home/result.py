from transformers import PreTrainedTokenizerFast
from transformers import GPT2LMHeadModel

def load_model(model_path):
    model=GPT2LMHeadModel.from_pretrained(model_path)
    return model

def load_tokenizer(tokenizer_path):
    tokenizer=PreTrainedTokenizerFast.from_pretrained(tokenizer_path)
    return tokenizer


def generate_text(sequence, max_length):
    model_path="/home/ubuntu/gbproject/home/trained/"
    model=load_model(model_path)
    tokenizer=load_tokenizer(model_path)
    ids=tokenizer.encode(f'{sequence}', add_special_tokens=False, return_tensors='pt')
    final_outputs=model.generate(ids, do_sample=True, max_length=50, pad_token_id=model.config.pad_token_id, eos_token_id=model.config.eos_token_id,top_k=40, top_p=0.95) #조정하기
    result=tokenizer.decode(final_outputs[0], skip_special_tokens=True, clean_up_tokenization_spaces=True)
    result_oneline=result.split("\n")
    return result_oneline[0]


def input_data(input):
    sequence = input
    max_len = 50
    

    a=[]
    for i in range(5):
        a.append(generate_text(sequence, max_len))
    print(a)

    return a
