package com.anuj.stockwatchlist.service;

import java.util.List;

import com.anuj.stockwatchlist.dto.PaginatedResponse;
import com.anuj.stockwatchlist.dto.StockRequestDTO;
import com.anuj.stockwatchlist.dto.StockResponseDTO;
import com.anuj.stockwatchlist.models.Stock;

public interface StockService {

    StockResponseDTO saveStock(StockRequestDTO s, int user_id);

    StockResponseDTO deleteStockById(int id, int user_id);

    StockResponseDTO findByIdDTO(int id, int user_id);

    Stock findById(int id, int user_id);

    StockResponseDTO updateStockById(int id, StockRequestDTO s, int user_id);

    PaginatedResponse<StockResponseDTO> getStocks(String symbol, int page, int size, String[] sortBy,
            String direction[], int user_id);

    List<StockResponseDTO> findBySymbol(String symbol, int user_id);

}
