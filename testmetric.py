import pandas as pd
import numpy as np
from sklearn.metrics import f1_score, confusion_matrix
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf
from tensorflow.keras.models import load_model
from pykrx import stock
from datetime import datetime, timedelta

# 모델 불러오기
model = load_model('samsung_stock_lstm_model.h5')
print("Model loaded from samsung_stock_lstm_model.h5")

# 테스트 데이터 수집 및 전처리
def get_test_data(ticker, start_date, end_date):
    df = stock.get_market_ohlcv_by_date(start_date, end_date, ticker)
    df = df[['고가', '저가', '종가', '거래량']]
    return df

ticker = "005930"  # 삼성전자 티커
end_date = datetime.now().strftime("%Y-%m-%d")
start_date = (datetime.now() - timedelta(days=30)).strftime("%Y-%m-%d")  # 최근 60일 데이터

test_df = get_test_data(ticker, start_date, end_date)

# 데이터프레임 출력
print("Test DataFrame:")
print(test_df.head())

# 데이터 정규화
scaler = MinMaxScaler(feature_range=(0, 1))
scaled_test_data = scaler.fit_transform(test_df)

look_back = 1  # 예측에 사용할 이전 일수
predicted_classes = []
actual_classes = []

# 여러 번의 예측 수행
for i in range(look_back, len(test_df)):
    last_test_data = scaled_test_data[i-look_back:i]
    last_test_data = np.reshape(last_test_data, (1, look_back, 4))

    # 다음 날 종가 예측
    predicted_price = model.predict(last_test_data)
    predicted_price = scaler.inverse_transform(np.hstack([np.zeros((predicted_price.shape[0], 2)), predicted_price, np.zeros((predicted_price.shape[0], 1))]))[:, 2]

    # 예측된 종가와 실제 종가 비교
    actual_price = test_df.iloc[i]['종가']
    predicted_class = 1 if predicted_price > actual_price else 0
    actual_class = 1 if test_df.iloc[i]['종가'] > test_df.iloc[i-1]['종가'] else 0

    predicted_classes.append(predicted_class)
    actual_classes.append(actual_class)

# 성능 지표 계산
f1 = f1_score(actual_classes, predicted_classes)
cm = confusion_matrix(actual_classes, predicted_classes, labels=[0, 1])

print(f"\nF1 Score: {f1}")
print("\nConfusion Matrix:")
print(cm)
