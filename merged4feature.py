import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf
from tensorflow.keras.models import Sequential, load_model
from tensorflow.keras.layers import Dense, LSTM

# 데이터 로드
df = pd.read_csv('samsung_stock_data.csv', index_col=0, parse_dates=True)

# 데이터프레임 출력
print("Original DataFrame:")
print(df.head())

# 필요한 컬럼만 추출
data = df[['고가', '저가', '종가', '거래량']]

# 데이터 정규화
scaler = MinMaxScaler(feature_range=(0, 1))
scaled_data = scaler.fit_transform(data)

# 정규화된 데이터프레임 출력
scaled_df = pd.DataFrame(scaled_data, index=data.index, columns=['고가', '저가', '종가', '거래량'])
print("Scaled DataFrame:")
print(scaled_df.head())

# 시계열 데이터 형식으로 변환
def create_dataset(dataset, look_back=1):
    X, Y = [], []
    for i in range(len(dataset)-look_back-1):
        a = dataset[i:(i+look_back)]
        X.append(a)
        Y.append(dataset[i + look_back, 2])  # 종가 예측
    return np.array(X), np.array(Y)

look_back = 1
X, Y = create_dataset(scaled_data, look_back)

# 데이터 차원 맞추기
X = np.reshape(X, (X.shape[0], X.shape[1], X.shape[2]))

# 변환된 데이터프레임 출력
print("Transformed Data (first 5 samples):")
print(f"X: {X[:5]}")
print(f"Y: {Y[:5]}")

# LSTM 모델 생성
model = Sequential()
model.add(LSTM(50, return_sequences=True, input_shape=(look_back, 4)))
model.add(LSTM(50))
model.add(Dense(1))

# 모델 컴파일
model.compile(loss='mean_squared_error', optimizer='adam')

# 모델 학습
history = model.fit(X, Y, epochs=100, batch_size=1, verbose=2)

# 모델 학습 손실 값 출력
print("Model Training Metrics:")
print("Loss:", history.history['loss'][-1])

# 모델 저장
model.save('samsung_stock_lstm_model.h5')
print("Model saved to samsung_stock_lstm_model.h5")

# 모델 불러오기
model = load_model('samsung_stock_lstm_model.h5')
print("Model loaded from samsung_stock_lstm_model.h5")

# 데이터 준비 (마지막 데이터를 사용하여 다음날 예측)
last_data = scaled_data[-look_back:]
last_data = np.reshape(last_data, (1, look_back, 4))

# 다음 날 종가 예측
predicted_price = model.predict(last_data)
predicted_price = scaler.inverse_transform(np.hstack([np.zeros((predicted_price.shape[0], 2)), predicted_price, np.zeros((predicted_price.shape[0], 1))]))[:, 2]

print(f"Predicted next day's closing price: {predicted_price[0]}")
