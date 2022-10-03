package com.example.cryptobot.controller;

import com.example.cryptobot.domain.*;
import com.example.cryptobot.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "api")
public class Controller {
    private final Service service;
    private final int limit = 1000;
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd[ [HH][:mm][:ss][.SSS]]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    @Autowired
    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping("/candlesticks")
    public List<CandleStick> getCandleSticks(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate,
            @RequestParam(name = "interval") String chartInterval,
            @RequestParam(name = "currencyBefore") String cBefore,
            @RequestParam(name = "currencyAfter") String cAfter) {

        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        ChartInterval interval = ChartInterval.getInstanceByCodeInterval(chartInterval);
        Currency currencyBefore = Currency.valueOf(cBefore);
        Currency currencyAfter = Currency.valueOf(cAfter);
        String symbol = Currency.getSymbol(currencyBefore, currencyAfter);

        return service.extractCandles(start, end, symbol, interval, limit);
    }

    @GetMapping("/stocks")
    public List<Stock> getStocks(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate,
            @RequestParam(name = "interval") String chartInterval,
            @RequestParam(name = "currencyBefore") String cBefore,
            @RequestParam(name = "currencyAfter") String cAfter) {

        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        ChartInterval interval = ChartInterval.getInstanceByCodeInterval(chartInterval);
        Currency currencyBefore = Currency.valueOf(cBefore);
        Currency currencyAfter = Currency.valueOf(cAfter);
        String symbol = Currency.getSymbol(currencyBefore, currencyAfter);

        return service.extractStocks(start, end, symbol, interval, limit);
    }

    @GetMapping("/test")
    public List<String> test(){
        List<String> test = new ArrayList<>();
        test.add("un");
        test.add("un3");
        return test;
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions(){
        return service.getTransactions();
    }

    @GetMapping("/prices")
    public List<Object> getPrices(){
        return this.service.getCryptoPrices();
    }

    @GetMapping("/startBot")
    public List<Object> startBot() {
        System.out.println("Start bot");
        this.service.startBollingerScript();
        return new ArrayList<>();
    }

    @GetMapping("/stopBot")
    public List<Object> stopBot() {
        System.out.println("Stop bot");
        this.service.stopBollingerScript();
        return new ArrayList<>();
    }


}
