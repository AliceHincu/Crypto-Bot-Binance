# Crypto-Bot-Binance
A bot for [Binance](https://www.binance.com/en) that uses Bollinger Bands strategies to make money. It was made in three days in a hackathon from my internship at TORA Trading Services.

# DEMO showing bot trading
* The demo is sped up from 30 minutes to showcase its functionality. 
* The buy/sell percentage is set up to 0.1% in this video.

https://user-images.githubusercontent.com/53339016/193571929-6b4498fe-49d5-4e3c-a6f3-3db7eda003ff.mp4

# Motivation: Why Bollinger Bands and Why a Bot?
**To make money.**

Jokes aside: The main theme for this hackathon was to do **anything we want with Binance/Kraken's APIs** and to **have REST with Java**, so to do an application with crypto currencies. I have always wanted to try creating a bot for trading, and I thought: what better time than know? 

## Day 1
The first challenge was the fact that **I didn't know anything about trading**, not a single term. So, for my first task, I reserved some of my time to learn some terms. After that, I searched what I needed to know for a trading bot. Then I found a keyword: **TRADING STRATEGIES**. 

The next Google search was _"What is the easiest trading strategy to learn?"_ and the answer was: **Bollinger Bands strategy**. I had no idea at that time what those words meant, and the internet didn't help, since there were a lot of new terms...especially because the strategy's idea is **BUY HIGH SELL LOW**, which is the opposite of what you should always do in trading. Also, these strategies usually work for stocks, not crypto, since crypto is very unpredictable.

Fortunately, I found a (video)[https://www.youtube.com/watch?v=2G9fHBEsauE] explaining much better the strategy. I also found a lot of videos showing in **Python** how this strategy should be implementing, using different libraries, and I merged them. As a database for my transactions, I chose **PostgreSQL**.

So in my first day, I had the python code ready.

## Day 2
The most difficult part in day 2 was to create a process from java that calls the python script and is always running until I stop it. I spent a lot of hours only to find out that I had multiple versions of python on my laptop and I didn't call the right one. After some configurations, I managed to repair the setup for python and make the calling of the script work. 

The backend was made with java using **SpringBoot**. I searched on the internet other ideas, and after some coding I had an endpoint for starting/stoping the bot and some other endpoints that collected some data from the Binance API that I has to use them the next day.

## Day 3
Day 3 was about building the frontend and having a presentable website for showcase. The challenge here was to find a goos library for showing live data of a crypto currency. I also added some extra APIs in the backend since I had extra time.

## The end
It was a fun challenge and I learned a lot in these 3 days, besides how to work with websockets in python and with external APIs:
* work with small steps
* better refactor in the beginning than when it's too late
* do research at the start so you don't have to erase everything because of lack of knowledge.

Will I continue on this bot? Yes, I want to, but after I finish my last year of university and after I learned more about trading.
