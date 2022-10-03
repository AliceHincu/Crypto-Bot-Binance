import binance.client as cl
import pandas
import psycopg2 as psycopg2
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import json

import websocket

''' ---------- globals'''
data_frame = []

''' ---------- database stuff'''


def create_table_transactions():
    create_script = '''CREATE TABLE IF NOT EXISTS transactions (
                            id  int PRIMARY KEY, 
                            timeUnix bigint, 
                            type varchar(20), 
                            beforeCurrency varchar(20), 
                            beforeQuantity float , 
                            afterCurrency varchar(20), 
                            afterQuantity float )'''
    cur.execute(create_script)


def delete_table_data():
    delete_script = '''DELETE FROM transactions'''
    cur.execute(delete_script)


def insert_into_table_transactions(order, balances, before, after):
    insert_script = 'INSERT INTO transactions (id, timeunix, type, beforeCurrency, beforeQuantity, afterCurrency, ' \
                    'afterQuantity) VALUES (%s, %s, %s, %s, %s, %s, %s)'
    before_quantity = 0
    before_currency = 0
    after_quantity = 0
    after_currency = 0

    for balance in balances['balances']:
        if balance['asset'] == before:
            before_currency = before
            before_quantity = balance['free']
        elif balance['asset'] == after:
            after_currency = after
            after_quantity = balance['free']

    insert_value = (order['orderId'],
                    order['transactTime'],
                    order['side'],
                    before_currency, before_quantity,
                    after_currency, after_quantity)
    cur.execute(insert_script, insert_value)


''' ---------- bollinger stuff'''


def get_data_frame():
    start_time = '1 day ago UTC'  # to start for 1 day ago
    interval = '5m'
    bars = client.get_historical_klines(symbol, interval, start_time)

    for line in bars:  # only keep the first 5 columns: date, open, high, low, close
        del line[5:]

    df = pd.DataFrame(bars, columns=['date', 'open', 'high', 'low', 'close'])
    return df


def plot_graph(df):
    df = df.astype(float)
    df[['close', 'sma', 'upper', 'lower']].plot()
    plt.xlabel("Date", fontsize=18)
    plt.ylabel('Close price', fontsize=18)
    x_axis = df.index
    plt.fill_between(x_axis, df['lower'], df['upper'], color='grey', alpha=0.30)

    plt.scatter(df.index, df['buy'], color='purple', label='Buy', marker='^', alpha=1)  # purple = buy
    plt.scatter(df.index, df['sell'], color='red', label='Sell', marker='v', alpha=1)  # red = sell

    plt.show()


def buy_or_sell(df):
    buy_list = pd.to_numeric(df['buy'], downcast='float')
    sell_list = pd.to_numeric(df['sell'], downcast='float')

    # get current price of the symbol
    current_price = client.get_symbol_ticker(symbol=symbol)
    if float(current_price['price']) >= sell_list[-1]:  # sell order
        print("sell sell sell...")
        sell_order = client.order_market_sell(symbol=symbol, quantity=0.01)
        account = client.get_account()
        insert_into_table_transactions(sell_order, account, 'ETH', 'USDT')
        conn.commit()
    elif float(current_price['price']) <= buy_list[-1]:  # buy order
        print("buy buy buy....")
        buy_order = client.order_market_buy(symbol=symbol, quantity=0.01)
        account = client.get_account()
        insert_into_table_transactions(buy_order, account, 'USDT', 'ETH')
        conn.commit()
    else:
        print("...do nothing...")


def bollinger_trade_logic():
    global data_frame
    symbol_df = data_frame
    period = 20
    # small time Moving average, calculate 20 moving average using Pandas over close price
    symbol_df['sma'] = symbol_df['close'].rolling(period).mean()
    # get standard deviation
    symbol_df['std'] = symbol_df['close'].rolling(period).std()

    # calculate upper bollinger band -> upper = sma + 2 * sd
    symbol_df['upper'] = symbol_df['sma'] + (2 * symbol_df['std'])
    # calculate lower bollinger band -> lower = sma - 2 * sd
    symbol_df['lower'] = symbol_df['sma'] - (2 * symbol_df['std'])

    # to print in human readable date and time (from timestamp)
    symbol_df.set_index("date", inplace=True)
    symbol_df.index = pd.to_datetime(symbol_df.index, unit="ms")  # index set to first column = date_and_time

    # prepare buy and sell signals. The lists prepared are still panda dataframes with float numbers
    close_list = pd.to_numeric(symbol_df['close'], downcast='float')
    upper_list = pd.to_numeric(symbol_df['upper'], downcast='float')
    lower_list = pd.to_numeric(symbol_df['lower'], downcast='float')

    # formula: buy = if(close < lower) close, else nan
    symbol_df['buy'] = np.where(close_list < lower_list, symbol_df['close'], np.NaN)
    # formula: sell = if(close > lower) close, else nan
    symbol_df['sell'] = np.where(close_list > upper_list, symbol_df['close'], np.NaN)

    # with open("output.txt", "w") as f:
    #     f.write(symbol_df.to_string())

    return symbol_df


def main():
    bollinger_trade_logic()


def on_open(ws):
    print("--- OPEN ---")
    global data_frame
    data_frame = get_data_frame()


def on_close(ws):
    print("--- CLOSE ---")


def on_message(ws, message):
    global data_frame
    json_message = json.loads(message)

    candle = [[json_message["k"]["t"], json_message["k"]["o"], json_message["k"]["h"], json_message["k"]["l"],
               json_message["k"]["c"]]]

    df2 = pd.DataFrame(candle, columns=['date', 'open', 'high', 'low', 'close'])
    data_frame = pandas.concat([data_frame, df2])
    symbol_df = bollinger_trade_logic()
    buy_or_sell(symbol_df)


if __name__ == '__main__':
    symbol = 'ETHUSDT'
    api_url = "https://testnet.binance.vision/api"
    api_url_socket = "wss://testnet.binance.vision/ws"
    my_api_socket = api_url_socket + "/ethusdt@kline_1m"
    api_key = 'YI3RT7lm6tlOzzb6Xi8bNcHWI4JgeT690DqPnC5C3qyZx49JfJWwVryZyv5PkdXr'
    api_secret = 'E2peaB3xlf7VsqTFrmDBooXA6RNLRSu1CMnb437knXOiRWVnGkCXanfUYvCWqDUn'
    hostname = 'localhost'
    database = 'transactions'
    username = 'postgres'
    pwd = 'password'
    port_id = 5432
    conn = None
    cur = None

    try:
        # database connection
        conn = psycopg2.connect(
            host=hostname,
            dbname=database,
            user=username,
            password=pwd,
            port=port_id
        )
        cur = conn.cursor()

        # api connection
        client = cl.Client(api_key, api_secret, testnet=True)

        ws = websocket.WebSocketApp(my_api_socket, on_open=on_open, on_close=on_close, on_message=on_message)
        ws.run_forever()
    except Exception as error:
        print(error)
    finally:
        conn.commit()
        if cur is not None:
            cur.close()
        if conn is not None:
            conn.close()
