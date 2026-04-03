package com.anuj.stockwatchlist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anuj.stockwatchlist.dto.StockPriceResponse;
import com.anuj.stockwatchlist.service.StockPriceService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    StockPriceService priceService;

    @GetMapping("/{symbol}")
    public StockPriceResponse getPrice(@PathVariable String symbol) {
        return priceService.getPrice(symbol);
    }
}
