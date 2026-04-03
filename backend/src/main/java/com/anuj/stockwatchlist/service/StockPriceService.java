package com.anuj.stockwatchlist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.anuj.stockwatchlist.dto.StockPriceResponse;
import com.anuj.stockwatchlist.exceptions.StockNotFoundException;

@Service
public class StockPriceService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${finnhub.api.key}")
    private String apiKey;

    @Value("${finnhub.base.url}")
    private String baseUrl;

    public StockPriceResponse getPrice(String symbol) {
        String url = String.format("%s/quote?symbol=%s&token=%s", baseUrl, symbol, apiKey);
        StockPriceResponse response = restTemplate.getForObject(url, StockPriceResponse.class);

        if (response == null || response.getCurrentPrice() == 0 || response.getCurrentPrice() == null) {
            throw new StockNotFoundException("Stock not found with symbol:" + symbol);
        }
        return response;
    }
}
