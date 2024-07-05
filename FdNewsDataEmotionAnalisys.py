import pandas as pd
import numpy as np
import requests
import json
from bs4 import BeautifulSoup
from datetime import datetime, timedelta
from transformers import AutoTokenizer, AutoModelForSequenceClassification
from transformers import pipeline
from sklearn.model_selection import train_test_split,KFold,StratifiedKFold


tokenizer = AutoTokenizer.from_pretrained('snunlp/KR-FinBert-SC')
model = AutoModelForSequenceClassification.from_pretrained('snunlp/KR-FinBert-SC')

sentiClassifier = pipeline(task='text-classification', model=model, tokenizer=tokenizer)


#-----------------------------------------------------------------------------------------

# def get_posts_info(keyword, start_page=1, end_page=10):
#     url='https://section.blog.naver.com/ajax/SearchList.naver'
#     header = {
#         'Referer': 'https://section.blog.naver.com/Search/Post.naver',
#     }
#     end_date = datetime.today()
#     start_date = end_date - timedelta(days=7)
#     all_posts_info = []
#     print(f' [INFO] keyword: {keyword}, start_page: {start_page}, end_page: {end_page}')
#     for i in range(start_page, end_page+1):
#         params = {
#             'countPerPage': 7,
#             'currentPage': i,
#             'endDate': end_date.strftime('%Y-%m-%d'),
#             'keyword': keyword,
#             'orderBy': 'sim',
#             'startDate': start_date.strftime('%Y-%m-%d'),
#             'type': 'post',
#         }
#         res = requests.get(url, params=params, headers=header)
#         current_posts_info = json.loads(res.text.split('\n')[1])['result']['searchList']
#         all_posts_info = current_posts_info
#         print(f' [INFO] 포스트 정보 수집 중.. (page: {i}/{end_page}) current posts: ({len(current_posts_info)}) all posts: ({len(all_posts_info)})')
#     return all_posts_info
# 네이버 블로그 포스트에서 정보를 긁어오는 코드

# def get_posts(x):
#     url = 'https://blog.naver.com/PostView.naver'
#     params = {
#         'blogId': x['domainIdOrBlogId'],
#         'logNo': x['logNo'],
#         'redirect': 'Dlog',
#         'widgetTypeCall': 'true',
#         'directAccess': 'false'
#     }
#     res = requests.get(url, params=params)
#     soup = BeautifulSoup(res.text, 'lxml')
#     posts = soup.select_one('.se-main-container').select('p')
#     posts = [x.get_text().replace('\u200b', '') for x in posts]
#     filtered_posts = [x.replace('다.', '다. \n') for x in posts]
#     filtered_posts = sum([x.split('\n') for x in posts], [])
#     filtered_posts = [x.strip() for x in filtered_posts if not x in ['', '']]
    
#     return filtered_posts

# posts_info = get_posts_info('주가전망', 1, 10)

# x = get_posts(posts_info[0])
# y = [x['label'] for x in sentiClassifier(x)]

# for i in range(1, len(posts_info)):
    
#     df_next = pd.DataFrame(data={
#         'text': x,
#         'senti': y
#     })
    
#     df = pd.concat([df, df_next])
#     print(f'[INFO] 분류 작업 중.. (Already: {i}/{len(posts_info)-1} Complete: {len(df)})')
    
    
# df = df.reset_index(drop=True)
# print(df['senti'].value_counts() / len(df))

# ---------------네이버 블로그 '주가전망' 키워드 검색결과 긍정, 보통, 부정 분류-----------------

#

DATASET_NAME = 'finance_data.csv'
DATASET_PREP_FILE = 'dataset_prep.csv'

#----------------------------------------------------dataset preb
# dataSet = pd.read_csv(DATASET_NAME)
# dataSet.head()

# del dataSet['sentence']

# dataSet.drop_duplicates(subset = ['kor_sentence'], inplace=True)
# dataSet.to_csv(DATASET_PREP_FILE)
#-----------------------------------------------------------------

dataset = pd.read_csv(DATASET_PREP_FILE)
dataset.drop('Unnamed: 0', axis=1, inplace=True)

XData = dataset['kor_sentence']
YData = dataset['labels']

TEST_SIZE = 0.2
RANDOM_STATE = 42

XTrain, XTest, YTrain, YTest = train_test_split(XData, YData, 
                                                test_size=TEST_SIZE, 
                                                random_state=RANDOM_STATE,
                                                stratify=YData)

print(f'exp input data : {len(XTrain)}')
print(YTrain.value_counts(normalize = True))
print(f'test input data : {len(XTest)}')
print(YTest.value_counts(normalize = True))

data_test = pd.read_csv(DATASET_PREP_FILE)

result_df = pd.DataFrame(columns=['labels', 'kor_sentence'])

for i in range(len(data_test)):
    x = data_test.loc[i, 'kor_sentence']
    y = sentiClassifier(x)[0]['label']
    
    df_next = pd.DataFrame(data={
        'labels': [y],
        'kor_sentence':[x]
    })
    
    result_df = pd.concat([result_df, df_next], ignore_index=True)
    print(f'Already: {i+1}/{len(data_test)} Complete: {len(data_test)}')
    
results_df = result_df.reset_index(drop=True)
print(results_df['labels'].value_counts() / len(results_df))