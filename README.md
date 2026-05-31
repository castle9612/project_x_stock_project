# Mock Stock Prediction System

주식 가격 예측 아이디어 검증을 위한 LSTM 실험 코드와 API 요청 예제를 정리한 프로젝트입니다.

## Tech Stack

- Python
- TensorFlow / Keras
- pandas / NumPy
- scikit-learn
- matplotlib
- requests

## Features

- Samsung stock CSV 기반 LSTM 예측 실험
- 학습/테스트 구간 정규화 및 시계열 데이터셋 변환
- 예측 API 호출용 샘플 클라이언트 포함

## Results / Highlights

- 주가 CSV를 정규화한 뒤 look-back window 형태의 LSTM 입력으로 변환하는 실험 코드를 구현했습니다.
- 학습/테스트 구간에 대해 MSE를 출력하고, 실제값과 예측값 비교 그래프를 `outputs/lstm_prediction.png`로 저장합니다.
- API 서버와 분리된 예측 요청 클라이언트를 제공해 모델 서빙 구조를 가정한 호출 흐름을 검증할 수 있게 했습니다.
- 공개용 코드에서는 `High`, `Low`, `Close`, `Volume` 또는 한국어 컬럼명을 모두 지원하도록 정리했습니다.

## Project Structure

```text
.
├── docs/
│   └── idea.md
├── examples/
│   ├── predict_request.py
│   └── sample_request.json
├── src/
│   └── lstm_stock_predictor.py
├── requirements.txt
└── README.md
```

## Private Data

주가 CSV 데이터는 포함하지 않습니다. 로컬 실행 시 아래 위치에 배치합니다.

```text
data/
├── samsung_stock_data.csv
└── samsung_stock_test_data.csv
```

CSV columns should use one of these schemas: `High`, `Low`, `Close`, `Volume`; lowercase equivalents; or Korean column names `고가`, `저가`, `종가`, `거래량`.

## Setup

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

## Usage

Run the LSTM experiment:

```bash
python src/lstm_stock_predictor.py
```

Call a prediction API:

```bash
PREDICT_API_URL=http://localhost:6000/predict python examples/predict_request.py
```

Windows PowerShell:

```powershell
$env:PREDICT_API_URL="http://localhost:6000/predict"
python examples/predict_request.py
```

## Notes

- `examples/predict_request.py` is a client example. The API server implementation is not included in this repository.
- `data/`, generated outputs, local models, and logs are ignored by Git.

## Lessons / Improvements

- 시계열 예측에서는 look-back window 구성과 inverse scaling 처리를 명확히 분리해야 결과 해석이 쉬워집니다.
- 다음 단계에서는 walk-forward validation, baseline comparison, model checkpoint 저장, API server 구현을 추가할 수 있습니다.
