from flask import Flask, request, jsonify
import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf
from tensorflow.keras.models import load_model

app = Flask(__name__)

# LSTM 모델 로드
model = load_model('samsung_stock_lstm_model.h5')


# 주식 데이터 로드 및 전처리 함수
def load_stock_data():
    df = pd.read_csv('samsung_stock_data.csv', index_col=0, parse_dates=True)
    return df


# 데이터 전처리 함수
def preprocess_data(data):
    # 스케일링을 위한 MinMaxScaler 적용
    scaler = MinMaxScaler(feature_range=(0, 1))
    scaled_data = scaler.fit_transform(data[['고가', '저가', '종가', '거래량']])
    return scaler, scaled_data


# 특정 날짜 범위의 데이터를 가져오는 함수
def get_data_by_date_range(df, start_date, end_date):
    mask = (df.index >= start_date) & (df.index <= end_date)
    return df.loc[mask]


# 예측 엔드포인트
@app.route('/predict', methods=['POST'])
def predict():
    # POST 요청으로부터 데이터를 가져옴
    req_data = request.get_json()
    start_date = req_data['start_date']
    end_date = req_data['end_date']

    # 주식 데이터 로드 및 특정 날짜 범위의 데이터 가져오기
    df = load_stock_data()
    selected_data = get_data_by_date_range(df, start_date, end_date)

    # 데이터 전처리
    scaler, scaled_data = preprocess_data(selected_data)

    # LSTM 입력 형식에 맞게 데이터 준비
    look_back = 1
    last_data = scaled_data[-look_back:]
    last_data = np.reshape(last_data, (1, look_back, 4))

    # 다음 날 종가 예측
    predicted_price = model.predict(last_data)
    predicted_price = scaler.inverse_transform(
        np.hstack([np.zeros((predicted_price.shape[0], 2)), predicted_price, np.zeros((predicted_price.shape[0], 1))]))[
                      :, 2]

    # 예측 결과와 선택된 데이터를 JSON 형식으로 반환
    # 인덱스를 문자열로 변환
    selected_data_json = selected_data.reset_index().to_json(orient='records')

    return jsonify({
        'predicted_price': predicted_price[0]
    })


if __name__ == '__main__':
    app.run(debug=True)
