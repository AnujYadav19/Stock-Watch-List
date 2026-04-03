package com.anuj.stockwatchlist.service;

import java.util.List;

import com.anuj.stockwatchlist.dto.PaginatedResponse;
import com.anuj.stockwatchlist.dto.StockRequestDTO;
import com.anuj.stockwatchlist.dto.StockResponseDTO;
import com.anuj.stockwatchlist.models.Stock;

public interface StockService {
    List<Stock> getAllStock();

    StockResponseDTO saveStock(StockRequestDTO s);

    StockResponseDTO deleteStockById(int id);

    StockResponseDTO findByIdDTO(int id);

    Stock findById(int id);

    StockResponseDTO updateStockById(int id, StockRequestDTO s);

    PaginatedResponse<StockResponseDTO> getStocks(String symbol, int page, int size, String[] sortBy,
            String direction[]);

    StockResponseDTO findBySymbol(String symbol);

}
