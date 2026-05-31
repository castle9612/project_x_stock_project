import json
import os

import requests


url = os.getenv("PREDICT_API_URL", "http://localhost:6000/predict")

payload = {
    "data": [
        {"Date": "2023-07-01", "High": 63000, "Low": 62000, "Close": 62500, "Volume": 1234567},
        {"Date": "2023-07-02", "High": 64000, "Low": 63000, "Close": 63500, "Volume": 2345678},
        {"Date": "2023-07-03", "High": 65000, "Low": 64000, "Close": 64500, "Volume": 3456789},
        {"Date": "2023-07-04", "High": 66000, "Low": 65000, "Close": 65500, "Volume": 4567890},
        {"Date": "2023-07-05", "High": 70000, "Low": 30000, "Close": 40000, "Volume": 5678901},
    ]
}

headers = {"Content-Type": "application/json"}
response = requests.post(url, headers=headers, data=json.dumps(payload), timeout=30)

print(response.status_code)
print(response.json())
