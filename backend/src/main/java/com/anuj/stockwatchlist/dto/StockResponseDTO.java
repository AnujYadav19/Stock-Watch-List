package com.anuj.stockwatchlist.dto;

public class StockResponseDTO {
    private int id;
    private String symbol;

    public StockResponseDTO() {
    }

    public StockResponseDTO(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "StockDTO [id=" + id + ", symbol=" + symbol + "]";
    }

}
