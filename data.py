import pandas as pd
from pykrx import stock
from sklearn.preprocessing import MinMaxScaler

# 데이터 수집을 위한 변수 설정
ticker = "005930"  # 삼성전자 티커
start_date = "2023-07-01"  # 데이터 시작 날짜
end_date = "2024-06-01"  # 데이터 종료 날짜

# 주식 데이터 수집
df = stock.get_market_ohlcv_by_date(start_date, end_date, ticker)

# 수집된 데이터 확인
print(df.head())

# 데이터프레임을 CSV 파일로 저장
csv_file_path = "samsung_stock_data.csv"
df.to_csv(csv_file_path, encoding='utf-8-sig')

print(f"Data saved to {csv_file_path}")
