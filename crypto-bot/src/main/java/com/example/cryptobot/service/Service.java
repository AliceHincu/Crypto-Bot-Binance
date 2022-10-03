package com.example.cryptobot.service;

import com.example.cryptobot.algorithms.ScriptPython;
import com.example.cryptobot.domain.CandleStick;
import com.example.cryptobot.domain.ChartInterval;
import com.example.cryptobot.domain.Stock;
import com.example.cryptobot.domain.Transaction;
import com.example.cryptobot.repository.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Slf4j
public class Service {
    private final String apiUrl = "https://api.binance.com";
    private final String candleUrl = "/api/v3/klines";
    private final String currencyPrice = "/api/v3/ticker/price";
    private final List<String> tempCurrencySymbols = new ArrayList<>();
    private final String bollingerBandsScriptPath = new File("./").getCanonicalPath() + "\\src\\main\\java\\com\\example\\cryptobot\\algorithms\\BollingerBands.py";
    private final ScriptPython bollingerBandsScript;
    private final Repository transactionRepository;

    @Autowired
    public Service(Repository transactionRepository) throws IOException {
        this.transactionRepository = transactionRepository;
        this.bollingerBandsScript = new ScriptPython(bollingerBandsScriptPath);
        this.tempCurrencySymbols.add("BTCUSDT");
        this.tempCurrencySymbols.add("LTCUSDT");
        this.tempCurrencySymbols.add("ETHUSDT");
        this.tempCurrencySymbols.add("XRPUSDT");
        this.tempCurrencySymbols.add("DOGEUSDT");
        this.tempCurrencySymbols.add("SHIBUSDT");
    }

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public void startBollingerScript() {
        try {
            this.bollingerBandsScript.runScript();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stopBollingerScript() {
        this.bollingerBandsScript.stopScript();
    }

    public List<CandleStick> extractCandles(LocalDateTime begin, LocalDateTime end, String symbol, ChartInterval interval, int limit) {
        RestTemplate rt = new RestTemplate();
        URI url = this.buildCandleURI(begin, end, symbol, interval, limit);

        ResponseEntity<List<List<Object>>> exchange = rt.exchange(new RequestEntity<>(HttpMethod.GET, url),
                new ParameterizedTypeReference<>() {
                });
        List<List<Object>> response = exchange.getBody();

        if (response == null) {
            return new ArrayList<>();
        }

        return response.stream()
                .map(l -> CandleStick.fromArray(l, symbol, interval))
                .collect(Collectors.toList());
    }

    public List<Stock> extractStocks(LocalDateTime begin, LocalDateTime end, String symbol, ChartInterval interval, int limit) {
        RestTemplate rt = new RestTemplate();
        URI url = this.buildCandleURI(begin, end, symbol, interval, limit);

        ResponseEntity<List<List<Object>>> exchange = rt.exchange(new RequestEntity<>(HttpMethod.GET, url),
                new ParameterizedTypeReference<>() {
                });
        List<List<Object>> response = exchange.getBody();

        if (response == null) {
            return new ArrayList<>();
        }

        return response.stream()
                .map(l -> Stock.fromArray(l))
                .collect(Collectors.toList());
    }

    public List<Object> getCryptoPrices() {
        RestTemplate rt = new RestTemplate();
        List<Object> result = new ArrayList<>();
        for(String currency: this.tempCurrencySymbols){
            URI url = this.buildSymbolPriceURI(currency);
            var exchange = rt.exchange(new RequestEntity<>(HttpMethod.GET, url),
                    new ParameterizedTypeReference<>() {
                    });
            var response = exchange.getBody();
            result.add(response);
        }
        return result;
    }


    private URI buildCandleURI(LocalDateTime begin, LocalDateTime end, String symbol, ChartInterval interval, int limit) {
        return UriComponentsBuilder.fromHttpUrl(apiUrl + candleUrl)
                .queryParam("startTime"
                        , begin.toEpochSecond(ZoneOffset.UTC) * 1000)
                .queryParam("symbol", symbol)
                .queryParam("interval", interval.getCode())
                .queryParam("limit", limit)
                .queryParam("endTime",
                        end.toEpochSecond(ZoneOffset.UTC) * 1000)
                .build().toUri();
    }

    private URI buildSymbolPriceURI(String symbol){
        return UriComponentsBuilder.fromHttpUrl(apiUrl + currencyPrice)
                .queryParam("symbol", symbol)
                .build().toUri();
    }
}
