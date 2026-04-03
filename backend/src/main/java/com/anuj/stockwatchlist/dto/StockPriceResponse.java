package com.anuj.stockwatchlist.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class StockPriceResponse {
    @JsonAlias("c")
    Double currentPrice;

    @JsonAlias("dp")
    Double percentChange;

    public StockPriceResponse() {
    }

    public StockPriceResponse(Double currentPrice, Double percentChange) {
        this.currentPrice = currentPrice;
        this.percentChange = percentChange;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(Double percentChange) {
        this.percentChange = percentChange;
    }

    @Override
    public String toString() {
        return "StockPriceResponse [currentPrice=" + currentPrice + ", percentChange=" + percentChange + "]";
    }

}
