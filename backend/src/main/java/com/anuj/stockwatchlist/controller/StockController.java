package com.anuj.stockwatchlist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anuj.stockwatchlist.dto.ApiResponse;
import com.anuj.stockwatchlist.dto.PaginatedResponse;
import com.anuj.stockwatchlist.dto.StockRequestDTO;
import com.anuj.stockwatchlist.dto.StockResponseDTO;
import com.anuj.stockwatchlist.service.StockService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class StockController {

    @Autowired
    StockService service;

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @GetMapping("/")
    public String homePage() {
        return "Welcome to the home page!";
    }

    // @GetMapping("/stocks")
    // public List<Stock> listStocks() {
    // return service.getAllStock();
    // }

    @GetMapping("/stocks")
    public ApiResponse<PaginatedResponse<StockResponseDTO>> listStocksPaginated(
            @RequestParam(defaultValue = "", required = false) String symbol,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "symbol") String sortBy[],
            @RequestParam(defaultValue = "asc") String direction[],
            @RequestHeader(value = "X-User-Id") int userId) {
        logger.info("GET /stocks request received with symbol : {}", symbol);
        PaginatedResponse<StockResponseDTO> response = service.getStocks(symbol, page, size, sortBy, direction, userId);
        logger.info("GET /stocks completed with {} stocks retrieved", response.getTotalElements());
        return new ApiResponse<>(true, "Stock(s) retrieved successfully", response);

    }

    @GetMapping("/stocks/search")
    public ApiResponse<List<StockResponseDTO>> getStockBySymbol(@RequestParam String symbol,
            @RequestHeader(value = "X-User-Id") int userId) {
        logger.info("GET /stocks/search request received with symbol: {}", symbol);
        List<StockResponseDTO> response = service.findBySymbol(symbol, userId);
        logger.info("GET /stocks/search completed successfully for {} stocks", response.size());
        return new ApiResponse<>(true, "Stock retrieved successfully", response);

    }

    @GetMapping("/stocks/{id}")
    public ApiResponse<StockResponseDTO> getStock(@PathVariable int id,
            @RequestHeader(value = "X-User-Id") int userId) {
        logger.info("GET /stocks/{id} request received with id: {}", id);
        StockResponseDTO response = service.findByIdDTO(id, userId);
        logger.info("GET /stocks/{id} completed successfully with id:{}", response.getId());
        return new ApiResponse<>(
                true,
                "Stock retrieved successfully",
                response);
    }

    @PostMapping("/stocks")
    public ApiResponse<StockResponseDTO> addStock(@Valid @RequestBody StockRequestDTO s,
            @RequestHeader(value = "X-User-Id") int userId) {
        logger.info("POST /stocks request received with symbol: {}", s.getSymbol());
        StockResponseDTO response = service.saveStock(s, userId);
        logger.info("POST /stocks completed successfully with id : {}", response.getId());
        return new ApiResponse<>(true, "Stock created successfully", response);
    }

    @PutMapping("/stocks/{id}")
    public ApiResponse<StockResponseDTO> updateStock(@Valid @RequestBody StockRequestDTO s, @PathVariable int id,
            @RequestHeader(value = "X-User-Id") int userId) {
        logger.info("PUT /stocks/{id} request received with id: {}, symbol: {}", id, s.getSymbol());
        StockResponseDTO response = service.updateStockById(id, s, userId);
        logger.info("PUT /stocks/{id} completed successfully with id : {}", response.getId());
        return new ApiResponse<>(true, "Stock updated successfully", response);

    }

    @DeleteMapping("/stocks/{id}")
    public ApiResponse<StockResponseDTO> deleteStock(@PathVariable int id,
            @RequestHeader(value = "X-User-Id") int userId) {
        logger.info("DELETE /stocks/{id} request received with id: {}", id);
        StockResponseDTO response = service.deleteStockById(id, userId);
        logger.info("DELETE /stocks/{id} completed successfully with id : {}", id);
        return new ApiResponse<>(true, "Stock deleted successfully", response);

    }
}
