package com.example.cryptobot.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://binance-docs.github.io/apidocs/spot/en/#kline-candlestick-streams">Kline/Candlestick Streams API Documentation</a>
 */
@Getter
public enum ChartInterval {
    ONE_MIN("1m"),
    THREE_MIN("3m"),
    FIVE_MIN("5m"),
    FIFTEEN_MIN("15m"),
    THIRTY_MIN("30m"),
    ONE_HOUR("1h"),
    TWO_HOUR("2h"),
    FOUR_HOUR("4h"),
    SIX_HOUR("6h"),
    EIGHT_HOUR("8h"),
    TWELVE_HOUR("12h"),
    ONE_DAY("1d"),
    THREE_DAY("3d"),
    ONE_WEEK("1w"),
    ONE_MONTH("1M");
    private final String code;

    ChartInterval(String code) {
        this.code = code;
    }

    public static ChartInterval getInstanceByCodeInterval(String code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode().equals(code))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No such value in the enum"));
    }
}
