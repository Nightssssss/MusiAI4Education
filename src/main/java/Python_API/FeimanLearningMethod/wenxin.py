import requests
import json

API_KEY = "dpDtO8esA1Xo3y6ZZOETGbPC"
SECRET_KEY = "Twqq79KEGa7gzbLF3LN1rfg3GcoqUQKa"


def get_access_token():
    """
    使用 API Key，Secret Key 获取access_token
    """

    url = f"https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials" \
          f"&client_id={API_KEY}&client_secret={SECRET_KEY}"

    payload = json.dumps("")
    headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }

    response = requests.request("POST", url, headers=headers, data=payload)
    access_token = response.json().get("access_token")
    return access_token


def get_response(prompt, access_token):
    url = f"https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?" \
          f"access_token={access_token}"

    payload = json.dumps({
        "temperature": 0.8,
        "top_p": 0.8,
        "messages": [
            {
                "role": "user",
                "content": prompt
            }
        ]
    })

    headers = {
        'Content-Type': 'application/json'
    }

    response = requests.request("POST", url, headers=headers, data=payload)
    response = response.json()["result"]
    # print("response:", response)
    return response
