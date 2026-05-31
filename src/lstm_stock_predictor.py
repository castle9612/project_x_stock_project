from pathlib import Path

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from sklearn.metrics import mean_squared_error
from sklearn.preprocessing import MinMaxScaler
from tensorflow.keras.layers import LSTM, Dense
from tensorflow.keras.models import Sequential


PROJECT_ROOT = Path(__file__).resolve().parents[1]
DATA_DIR = PROJECT_ROOT / "data"
OUTPUT_DIR = PROJECT_ROOT / "outputs"

TRAIN_FILE = DATA_DIR / "samsung_stock_data.csv"
TEST_FILE = DATA_DIR / "samsung_stock_test_data.csv"

FEATURE_COLUMN_CANDIDATES = [
    ["High", "Low", "Close", "Volume"],
    ["high", "low", "close", "volume"],
    ["고가", "저가", "종가", "거래량"],
]


def select_feature_columns(df):
    for columns in FEATURE_COLUMN_CANDIDATES:
        if all(column in df.columns for column in columns):
            return columns

    raise ValueError(
        "Input CSV must contain one of these column sets: "
        + ", ".join(str(columns) for columns in FEATURE_COLUMN_CANDIDATES)
    )


def create_dataset(dataset, target_index, look_back=1):
    x_values, y_values = [], []
    for i in range(len(dataset) - look_back - 1):
        x_values.append(dataset[i : i + look_back])
        y_values.append(dataset[i + look_back, target_index])
    return np.array(x_values), np.array(y_values)


def inverse_target_scale(values, scaler, n_features, target_index):
    values = np.asarray(values).reshape(-1, 1)
    padded = np.zeros((values.shape[0], n_features))
    padded[:, target_index] = values[:, 0]
    return scaler.inverse_transform(padded)[:, target_index]


def main():
    train_df = pd.read_csv(TRAIN_FILE, index_col=0, parse_dates=True)
    test_df = pd.read_csv(TEST_FILE, index_col=0, parse_dates=True)

    feature_columns = select_feature_columns(train_df)
    target_index = feature_columns.index("Close") if "Close" in feature_columns else 2

    train_data = train_df[feature_columns]
    test_data = test_df[feature_columns]

    scaler = MinMaxScaler(feature_range=(0, 1))
    scaled_train_data = scaler.fit_transform(train_data)
    scaled_test_data = scaler.transform(test_data)

    look_back = 1
    x_train, y_train = create_dataset(scaled_train_data, target_index, look_back)
    x_test, y_test = create_dataset(scaled_test_data, target_index, look_back)

    x_train = np.reshape(x_train, (x_train.shape[0], x_train.shape[1], x_train.shape[2]))
    x_test = np.reshape(x_test, (x_test.shape[0], x_test.shape[1], x_test.shape[2]))

    model = Sequential(
        [
            LSTM(50, return_sequences=True, input_shape=(look_back, len(feature_columns))),
            LSTM(50),
            Dense(1),
        ]
    )
    model.compile(loss="mean_squared_error", optimizer="adam")
    model.fit(x_train, y_train, epochs=100, batch_size=1, verbose=2, validation_data=(x_test, y_test))

    train_predict = model.predict(x_train)
    test_predict = model.predict(x_test)

    n_features = len(feature_columns)
    train_predict_inverse = inverse_target_scale(train_predict, scaler, n_features, target_index)
    y_train_inverse = inverse_target_scale(y_train, scaler, n_features, target_index)
    test_predict_inverse = inverse_target_scale(test_predict, scaler, n_features, target_index)
    y_test_inverse = inverse_target_scale(y_test, scaler, n_features, target_index)

    print(f"Train MSE: {mean_squared_error(y_train_inverse, train_predict_inverse):.2f}")
    print(f"Test MSE: {mean_squared_error(y_test_inverse, test_predict_inverse):.2f}")

    OUTPUT_DIR.mkdir(exist_ok=True)
    plt.figure(figsize=(12, 6))
    plt.plot(train_df.index[look_back + 1 : len(y_train) + look_back + 1], y_train_inverse, label="Train Actual")
    plt.plot(train_df.index[look_back + 1 : len(y_train) + look_back + 1], train_predict_inverse, label="Train Predict")
    plt.plot(test_df.index[look_back + 1 : len(y_test) + look_back + 1], y_test_inverse, label="Test Actual")
    plt.plot(test_df.index[look_back + 1 : len(y_test) + look_back + 1], test_predict_inverse, label="Test Predict")
    plt.xlabel("Date")
    plt.ylabel("Stock Price")
    plt.legend()
    plt.tight_layout()
    plt.savefig(OUTPUT_DIR / "lstm_prediction.png", dpi=150)


if __name__ == "__main__":
    main()
