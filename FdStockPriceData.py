import FinanceDataReader as fdr
import pandas as pd

krs_data = pd.read_csv('D:\gitlab\project_x\project_sun\krxCodeData.csv', encoding='UTF-8')

end_date = pd.to_datetime('2024-07-03')
start_date = end_date - pd.DateOffset(years=1)

result_list = []
count = 0

for inx, row in krs_data.iterrows():
    code = row['Code']
    name = row['Name']
    
    data = fdr.DataReader(code, start_date, end_date)
    
    data['High'] = data['High']
    data['Low'] = data['Low']
    data['Average'] = (data['High'] + data['Low']) / 2
    data['Code'] = code
    data['Name'] = name
    
    data['Date'] = data.index
    
    result_list.append(data[['Date', 'Code', 'Name', 'High', 'Low', 'Average']])
    
    count += 1
    if count == 100:
        break
    
result_df = pd.concat(result_list, ignore_index=True)
result_df.to_csv('KrxStockPriceData.csv', index=False)

print('총 {}개의 종목 월별 평균 수익률 저장완료.'.format(count))