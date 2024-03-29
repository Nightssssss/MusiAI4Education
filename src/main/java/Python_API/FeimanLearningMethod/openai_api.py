# coding:utf-8

import time
import openai
from openai.error import RateLimitError, Timeout, APIError, APIConnectionError

openai.api_key = "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"

delay_time = 0.5
decay_rate = 0.8


def get_responses_GPT(prompts: str, histories, model="gpt-3.5-turbo-0125", max_tokens=1024, temperature=0.8,
                      system_message=None, logprobs=None, echo=False):
    global delay_time, cur_key_idx

    # Wait for rate limit
    time.sleep(delay_time)

    # Send request
    try:
        results = []

        response = openai.ChatCompletion.create(
            model=model,
            messages=[
                {
                    "role": "system",
                    "content": system_message or "你是我的初中数学老师。"
                },
                *histories,
                {
                    "role": "user",
                    "content": prompts
                }
            ],
            temperature=temperature,
            max_tokens=max_tokens,
            top_p=1,
            frequency_penalty=0.0,
            presence_penalty=0.0,
            request_timeout=60
        )
        results.append(response["choices"][0])

        delay_time = max(delay_time * decay_rate, 0.1)
    except (RateLimitError, Timeout, APIError, APIConnectionError) as exc:
        print(openai.api_key, exc)
        delay_time = min(delay_time * 2, 30)
        return get_responses_GPT(prompts, model, max_tokens, temperature=temperature, system_message=system_message,
                                 histories=histories, logprobs=logprobs, echo=echo)
    except Exception as exc:
        print(exc)
        raise exc

    return results[0]["message"]["content"]
