package com.anuj.stockwatchlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class StockRequestDTO {

    @NotBlank(message = "Symbol cannot be blank")
    String symbol;

    @Positive(message = "buy price must be positive")
    Double buyPrice;

    @PositiveOrZero(message = "quantity must not be negative")
    Integer quantity;

    public StockRequestDTO() {
    }

    public StockRequestDTO(String symbol, Double buyPrice, Integer quantity) {
        this.symbol = symbol;
        this.buyPrice = buyPrice;
        this.quantity = quantity;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "StockRequestDTO [symbol=" + symbol + ", buyPrice=" + buyPrice + ", quantity=" + quantity + "]";
    }

}
