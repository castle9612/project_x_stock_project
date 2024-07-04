import numpy as np
import pandas as pd
import FinanceDataReader as fdr

krs_data = pd.read_csv('D:\gitlab\project_x\project_sun\krxCodeData.csv', encoding='UTF-8')

end_date = pd.to_datetime('2024-07-03')
start_date = end_date - pd.DateOffset(years=5)

count = 0
result_list = []

for inx, row in krs_data.iterrows():
    code = row['Code']
    name = row['Name']
    
    data = fdr.DataReader(code, start_date, end_date)
    data['Return'] = data['Close'].pct_change()
    data['Month'] = data.index.to_period('M')
    monthly_avg_return = data.groupby('Month')['Return'].mean()

    monthly_avg_return_df = monthly_avg_return.reset_index()
    monthly_avg_return_df['Code'] = code
    monthly_avg_return_df['Name'] = name
    
    monthly_avg_return_df = monthly_avg_return_df[['Month', 'Code', 'Name', 'Return']]
    result_list.append(monthly_avg_return_df)
    
    count += 1
    if count == 1000:
        break
    
result_df = pd.concat(result_list, ignore_index=True)
result_df.to_csv('KrxDataMean.csv', index=False)

print('총 {}개의 종목 월별 평균 수익률 저장완료.'.format(count))