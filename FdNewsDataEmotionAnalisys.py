import pandas as pd
import numpy as np
import requests
import json
from bs4 import BeautifulSoup
from datetime import datetime, timedelta
from transformers import AutoTokenizer, AutoModelForSequenceClassification
from transformers import pipeline

tokenizer = AutoTokenizer.from_pretrained('snumlp/KR-FinBert-SC')
model = AutoModelForSequenceClassification.from_pretrained('snumlp/KR-FinBert-SC')

sentiClassifier = pipeline(task='text-classification', model=model, tokenizer=tokenizer)

def get_posts_info(keyword, start_page=1, end_page=10):
    url='https://section.blog.naver.com/ajax/SearchList.naver'
    header = {
        'Referer': 'https://section.blog.naver.com/Search/Post.naver',
    }
    end_date = datetime.today()
    start_date = end_date - timedelta(days=7)
    all_posts_info = []
    print(f' [INFO] keyword: {keyword}, start_page: {start_page}, end_page: {end_page}')
    for i in range(start_page, end_page+1):
        params = {
            'countPerPage': 7,
            'currentPage': i,
            'endDate': end_date.strftime('%Y-%m-%d'),
            'keyword': keyword,
            'orderBy': 'sim',
            'startDate': start_date.strftime('%Y-%m-%d'),
            'type': 'post',
        }
        res = requests.get(url, params=params, headers=header)
        current_posts_info = json.loads(res.text.split('\n')[1])['result']['searchList']
        all_posts_info += current_posts_info
        print(f' [INFO] 포스트 정보 수집 중.. (page: {i}/{end_page}) current posts: ({len(current_posts_info)}) all posts: ({len(all_posts_info)})')
    return all_posts_info

def get_posts(x):
    url = 'https://blog.naver.com/PostView.naver'
    params = {
        'blogId': x['domainIdOrBlogId'],
        'logNo': x['logNo'],
        'redirect': 'Dlog',
        'widgetTypeCall': 'true',
        'directAccess': 'false'
    }
    res = requests.get(url, params=params)
    soup = BeautifulSoup(res.text, 'lxml')
    posts = soup.select_one('.se-main-container').select('p')
    posts = [x.get_text().replace('\u200b', '') for x in posts]
    filtered_posts = [x.replace('다.', '다. \n') for x in posts]
    filtered_posts = sum([x.split('\n') for x in posts], [])
    filtered_posts = [x.strip() for x in filtered_posts if not x in ['', '']]
    
    return filtered_posts

posts_info = get_posts_info('주가전망', 1, 10)

for i in range(1, len(posts_info)):
    x = get_posts(get_posts_info[i])
    y = [x['label'] for x in sentiClassifier(x)]
    
    df_next = pd.DataFrame(data={
        'text': x,
        'senti': y
    })
    
    df = pd.concat([df, df_next])
    print(f'[INFO] Analisys.. (Target posting: {i}/{len(posts_info)-1} Complete sentence : {len(df)})')
    
df = df.reset_index(drop=True)
print(df['senti'].value_counts() / len(df))