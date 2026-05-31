import csv
import os
from pathlib import Path

import mysql.connector
from mysql.connector import Error


PROJECT_ROOT = Path(__file__).resolve().parents[1]

class MySQLHelper:
    def __init__(self, host, user, password, database):
        self.host = host
        self.user = user
        self.password = password
        self.database = database
        self.connection = None

    def connect(self):
        try:
            self.connection = mysql.connector.connect(
                host=self.host,
                user=self.user,
                password=self.password,
                database=self.database
            )
        except Error as e:
            print(f'Error connecting to MySQL: {e}')
            raise

    def disconnect(self):
        if self.connection is not None and self.connection.is_connected():
            self.connection.close()

    def execute_query(self, query, data):
        if self.connection is not None and self.connection.is_connected():
            cursor = self.connection.cursor()
            try:
                cursor.execute(query, data)
                self.connection.commit()
            except Error as e:
                print(f'Error executing query: {e}')
                self.connection.rollback()
            finally:
                cursor.close()

def read_csv(file_path):
    data = []
    with open(file_path, mode='r', encoding='utf-8') as file:
        csv_reader = csv.DictReader(file)
        for row in csv_reader:
            weather_id = int(row['weather_id'])
            mars_date = row['mars_date']
            temp = int(row['temp'])
            storm = int(row['storm'])
            data.append((weather_id, mars_date, temp, storm))
    return data

def insert_data(helper, data):
    insert_query = '''
        INSERT INTO mars_weather (weather_id, mars_date, temp, storm)
        VALUES (%s, %s, %s, %s)
    '''
    for row in data:
        helper.execute_query(insert_query, row)

def main():
    db_host = os.getenv('MYSQL_HOST', '127.0.0.1')
    db_user = os.getenv('MYSQL_USER', 'root')
    db_password = os.getenv('MYSQL_PASSWORD', '')
    db_name = os.getenv('MYSQL_DATABASE', 'project_x')
    
    csv_file_path = os.getenv('MARS_WEATHER_CSV', str(PROJECT_ROOT / 'data' / 'mars_weathers_data.csv'))

    if not db_password:
        raise RuntimeError('MYSQL_PASSWORD environment variable is required.')

    mysql_helper = MySQLHelper(db_host, db_user, db_password, db_name)
    mysql_helper.connect()

    try:
        data = read_csv(csv_file_path)
        insert_data(mysql_helper, data)
        print('done')
    finally:
        mysql_helper.disconnect()

if __name__ == '__main__':
    main()
