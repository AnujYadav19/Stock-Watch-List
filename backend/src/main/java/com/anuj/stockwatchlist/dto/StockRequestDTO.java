package com.anuj.stockwatchlist.dto;

public class StockRequestDTO {

    String symbol;

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public StockRequestDTO() {
    }

    public StockRequestDTO(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "StockReqeustDTO [symbol=" + symbol + "]";
    }

}
