package com.example.cryptobot.domain;

import lombok.Getter;

@Getter
public enum Currency {
    /* --- crypto --- */
    BTC("BTC"),
    LTC("LTC"),
    ETH("ETH"),
    XRP("XRP"),
    DOGE("DOGE"),
    SHIB("SHIB"),
    /* --- money --- */
    USDT("USDT");
    private final String code;

    Currency(String code) {
        this.code = code;
    }
    public static String getSymbol(Currency currencyBefore, Currency currencyAfter){
        return currencyBefore.toString() + currencyAfter.toString();
    }
}
