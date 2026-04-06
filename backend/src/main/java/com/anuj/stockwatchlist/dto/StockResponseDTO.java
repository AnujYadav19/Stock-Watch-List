package com.anuj.stockwatchlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class StockResponseDTO {
    private int id;

    @NotBlank(message = "Symbol cannot be blank")
    private String symbol;

    @Positive(message = "buy price must be positive")
    private Double buyPrice;

    @PositiveOrZero(message = "quantity must not be negative")
    private Integer quantity;
    private String username;

    public StockResponseDTO() {
    }

    public StockResponseDTO(int id, String symbol, Double buyPrice, Integer quantity, String username) {
        this.id = id;
        this.symbol = symbol;
        this.buyPrice = buyPrice;
        this.quantity = quantity;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        return "StockResponseDTO [id=" + id + ", symbol=" + symbol + ", buyPrice=" + buyPrice + ", quantity=" + quantity
                + ", username=" + username + "]";
    }

}
