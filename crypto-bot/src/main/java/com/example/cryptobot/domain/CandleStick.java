package com.example.cryptobot.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Builder
@Data
public class CandleStick {
//    @Id
//    private String _id;
    private String symbol;
    private ChartInterval interval;

    private Long openTime;
    private Long closeTime;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal baseAssetVolume;
    private BigInteger numberOfTrades;
    private BigDecimal quoteAssetVolume;
    private BigDecimal takerBuyBaseAssetVolume;
    private BigDecimal takerBuyQuoteAssetVolume;
    private BigDecimal Ignore;

    public static CandleStick fromArray(List<Object> fields, String symbol, ChartInterval interval){
        int i = 0;

        return CandleStick.builder()
                .symbol(symbol)
                .interval(interval)
                .openTime((Long)fields.get(i++))
                .openPrice(new BigDecimal(fields.get(i++).toString()))
                .highPrice(new BigDecimal(fields.get(i++).toString()))
                .lowPrice(new BigDecimal(fields.get(i++).toString()))
                .closePrice(new BigDecimal(fields.get(i++).toString()))
                .baseAssetVolume(new BigDecimal(fields.get(i++).toString()))
                .closeTime((Long)fields.get(i++))
                .quoteAssetVolume(new BigDecimal(fields.get(i++).toString()))
                .numberOfTrades(BigInteger.valueOf(Long.parseLong(fields.get(i++).toString())))
                .takerBuyBaseAssetVolume(new BigDecimal(fields.get(i++).toString()))
                .takerBuyQuoteAssetVolume(new BigDecimal(fields.get(i++).toString()))
                .Ignore(new BigDecimal(fields.get(i).toString()))
                .build();
    }
}
