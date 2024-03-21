# coding:utf-8
import re
import sys
import io
from wenxin import *
from openai_api import get_responses_GPT

# 文心一言给出正解
def solve_wx(question):
    access_token = get_access_token()
    prompt_wx = question + "请直接给出正确解答，回答尽量简洁，只对关键步骤解释说明。"
    print("文心一言解答中...")
    response_wx = get_response(prompt_wx, access_token)
    print("文心一言解答完毕。")
    return response_wx


# 构建GPT4 prompt  分为两轮
def build_prompt_GPT(question, correct_answer,my_answer):
    prompt_GPT = open("G:\green-farm\src\main\java\Python_API\FeimanLearningMethod\prompt_GPT4.txt", "r", encoding="UTF-8").readlines()
    prompt_GPT[0] = prompt_GPT[0] + "请你仔细理解我的信息，如果你已经理解请回复'好的'。"
    prompt_GPT[1] = "这是我要向你询问的题目：" + question + prompt_GPT[0] + my_answer +"这道题的正确解法是：" + correct_answer + prompt_GPT[1]
    return prompt_GPT

def extract_content_between_braces(input_string):
    pattern = re.compile(r'\{([^}]+)\}')
    matches = pattern.findall(input_string)
    result = []
    for match in matches:
        wrapped_content = "{" + match + "}"
        result.append(wrapped_content)
    return result



if __name__ == "__main__":
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='gb18030')

    # 如果传入的参数大于1：将学生问题与大模型会话历史传入
    if len(sys.argv) == 4:

        question = sys.argv[1]
        history = sys.argv[2]
        
        histories_temp = extract_content_between_braces(history)

        histories = []
        for history in histories_temp:
            print(history)
            history = json.loads(history, strict=False)
            histories.append(history)

        prompt = question
        response = get_responses_GPT(prompts=prompt, histories=histories)
        print("ChatGPT4:", response)

        histories.append({"role": "user", "content": prompt})
        histories.append({"role": "assistant", "content": response})
        print(json.dumps(histories, ensure_ascii=False))

    elif len(sys.argv) == 3:

        my_answer = sys.argv[1]
        question_text = sys.argv[2]

        print("question：", question_text)
        correct_answer = solve_wx(question_text)

        prompt_GPT = build_prompt_GPT(question_text, correct_answer,my_answer)
        print("ChatGPT4分析中...")
        response = get_responses_GPT(prompts=prompt_GPT[0], histories=[])

        histories = []
        response = get_responses_GPT(prompts=prompt_GPT[1], histories=histories)

        print("ChatGPT4:", response)
        histories.append({"role": "user", "content": prompt_GPT[1]})
        histories.append({"role": "assistant", "content": response})

        print(json.dumps(histories, ensure_ascii=False))
