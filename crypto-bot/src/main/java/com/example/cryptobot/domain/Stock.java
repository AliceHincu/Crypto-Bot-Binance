package com.example.cryptobot.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Data
@Builder
public class Stock {
    private LocalDateTime time;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal volume;

    public static Stock fromArray(List<Object> fields){
        int i = 0;

        return Stock.builder()
                .time( LocalDateTime.ofInstant(Instant.ofEpochMilli((Long) fields.get(i++)),
                        TimeZone.getDefault().toZoneId()))
                .open(new BigDecimal(fields.get(i++).toString()))
                .high(new BigDecimal(fields.get(i++).toString()))
                .low(new BigDecimal(fields.get(i++).toString()))
                .close(new BigDecimal(fields.get(i++).toString()))
                .volume(new BigDecimal(fields.get(i).toString()))
                .build();
    }
}
