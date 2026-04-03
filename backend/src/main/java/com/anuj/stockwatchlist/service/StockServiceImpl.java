package com.anuj.stockwatchlist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anuj.stockwatchlist.dto.PaginatedResponse;
import com.anuj.stockwatchlist.dto.StockRequestDTO;
import com.anuj.stockwatchlist.dto.StockResponseDTO;
import com.anuj.stockwatchlist.exceptions.InvalidDirectionException;
import com.anuj.stockwatchlist.exceptions.InvalidSortFieldException;
import com.anuj.stockwatchlist.exceptions.StockNotFoundException;
import com.anuj.stockwatchlist.models.Stock;
import com.anuj.stockwatchlist.repository.StockRepository;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    ModelMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    @Override
    public List<Stock> getAllStock() {
        logger.info("getAllStock method started");
        List<Stock> stocks = stockRepository.findAll();
        logger.info("Retrieved {} stocks successfully", stocks.size());
        return stocks;
    }

    @Override
    public StockResponseDTO saveStock(StockRequestDTO s) {
        logger.info("saveStock method started with symbol: {}", s.getSymbol());
        // Stock theStock = mapStockRequestToStock(s);
        Stock theStock = mapper.map(s, Stock.class);
        // return mapStockToStockResponseDTO(stockRepository.save(theStock));
        logger.debug("Stock mapped to entity: {} ", theStock);
        StockResponseDTO savedStock = mapper.map(stockRepository.save(theStock), StockResponseDTO.class);
        logger.info("Stock saved successfully with id:{} ", theStock);
        return savedStock;
    }

    @Override
    public StockResponseDTO deleteStockById(int id) {
        StockResponseDTO theStock = findByIdDTO(id);
        stockRepository.deleteById(id);
        return theStock;
    }

    @Override
    public StockResponseDTO findByIdDTO(int id) {
        logger.info("findByIdDTO method started with id : {}", id);
        Optional<Stock> s = stockRepository.findById(id);

        if (s.isEmpty()) {
            logger.warn("Stock not found with the id: {}", id);
            throw new StockNotFoundException("No stock with this id: " + id);
        }
        logger.info("Stock retrieved successfully with id: {}", id);
        StockResponseDTO stock = mapper.map(s.get(), StockResponseDTO.class);

        logger.debug("Stock mapped to response dto for id: {}", id);

        return stock;

        // if (s.isEmpty()) {
        // throw new RuntimeException("No stock with this id: 5");
        // } else {
        // return s.get();
        // }
    }

    @Override
    public Stock findById(int id) {
        logger.info("findById method started with id:{}", id);
        Optional<Stock> s = stockRepository.findById(id);
        if (s.isEmpty()) {
            logger.warn("Stock not found with id:{}", id);
            throw new StockNotFoundException("No stock with this id: " + id);
        }
        logger.info("Stock retrieved successfully with id: {}", id);
        Stock stock = s.get();
        return stock;
        // if (s.isEmpty()) {
        // throw new RuntimeException("No stock with this id: 5");
        // } else {
        // return s.get();
        // }
    }

    @Override
    public StockResponseDTO updateStockById(int id, StockRequestDTO s) {
        logger.info("updateStockById method started with id: {}", id);
        Stock theStock = findById(id);
        logger.info("Stock retrieved successfully with id:{}", id);
        theStock.setSymbol(s.getSymbol());
        logger.debug("Stock fields updated for id: {}", id);

        Stock updatedStock = stockRepository.save(theStock);
        logger.info("Stock updated successfully with id: {}", id);

        // return mapStockToStockResponseDTO(updatedStock);
        StockResponseDTO mappedStock = mapper.map(updatedStock, StockResponseDTO.class);
        logger.debug("Stock entity successfully mapped to DTO for id: {}", id);
        return mappedStock;
    }

    @Override
    public PaginatedResponse<StockResponseDTO> getStocks(String symbol, int page, int size, String[] sortBy,
            String direction[]) {
        logger.info("getStocks method started with symbol: {}", symbol);
        Set<String> validFields = Set.of("symbol", "id");
        // logger.info("Set of valid fields created with {} fields",
        // validFields.size());
        for (String field : sortBy) {
            if (!validFields.contains(field.toLowerCase())) {
                logger.warn("Invalid sort field provided: {}", field);
                throw new InvalidSortFieldException("There is no field named: " + field);
            }
        }
        // logger.info("Validated {} field(s) in the sortBy array as valid)",
        // sortBy.length);
        // validate that no of fields and directions are the same
        if (sortBy.length != direction.length) {
            logger.warn("Mismatch between sort fields and directions");
            throw new InvalidDirectionException("No of fields are the not the same as no of directions");
        }

        // logger.info("Both no of fields and directions are {}, i.e. the same",
        // sortBy.length);

        List<Sort.Order> orders = new ArrayList<>();
        // logger.info("created a new list of sort.orders");

        for (int i = 0; i < sortBy.length; i++) {
            String dir = direction[i];
            if (!dir.equalsIgnoreCase("asc") && !dir.equalsIgnoreCase("desc")) {
                logger.warn("Invalid direction : {}", dir);
                throw new InvalidDirectionException(
                        "Invalid direction: " + dir);
            }
            Sort.Order order = new Order(Sort.Direction.fromString(dir), sortBy[i]);
            // logger.info("Created new order with direction and field");
            orders.add(order);
            logger.debug(
                    "Added sort order: {} {}",
                    sortBy[i],
                    dir);
        }

        Sort sortObject = Sort.by(orders);
        logger.debug("Sort object created with {} orders", orders.size());
        Pageable pageable = PageRequest.of(page, size, sortObject);
        logger.info("Pageable created with page={}, size={}, orders={}", page, size, orders.size());
        Page<Stock> stocks;

        if (symbol.trim().length() > 0) {
            logger.info("Fetching stocks containing symbol: {}", symbol);

            stocks = stockRepository.findBySymbolContaining(symbol, pageable);

        } else {
            logger.info("Fetching all stocks");
            stocks = stockRepository.findAll(pageable);
        }

        logger.info("Fetched {} stocks successfully", stocks.getNumberOfElements());

        Page<StockResponseDTO> pageList = mapPageStockToPageStockResponseDTO(stocks);
        return mapPageToPaginatedResponse(pageList);
    }

    @Override
    public StockResponseDTO findBySymbol(String symbol) {
        logger.info("findBySymbol method started with symbol : {}", symbol);
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock == null) {
            logger.warn("No stock found with symbol: {}", symbol);
            throw new StockNotFoundException("No stock with the symbol: " + symbol);
        }
        logger.info("Stock retrieved successfully with symbol: {}", symbol);
        StockResponseDTO stockResponseDTO = mapper.map(stock, StockResponseDTO.class);
        logger.debug("Stock mapped to DTO for symbol: {}", symbol);
        return stockResponseDTO;
    }

    // @Override
    // public Page<StockDTO> findBySymbolContaining(String symbol) {
    // Page<Stock> stocks = stockRepository.findBySymbolContaining(symbol);
    // return mapPageStockToPageStockDTO(stocks);
    // }

    // public StockResponseDTO mapStockToStockResponseDTO(Stock theStock) {
    // StockResponseDTO theStockResponseDTO = new StockResponseDTO();
    // theStockResponseDTO.setId(theStock.getId());
    // theStockResponseDTO.setSymbol(theStock.getSymbol());
    // return theStockResponseDTO;
    // }

    // public Page<StockResponseDTO> mapPageStockToPageStockResponseDTO(Page<Stock>
    // stocks) {
    // List<Stock> list = stocks.getContent();
    // List<StockResponseDTO> listDTO = new ArrayList<>();

    // for (Stock s : list) {
    // listDTO.add(mapper.map(s, StockResponseDTO.class));
    // }

    // return new PageImpl<>(listDTO, stocks.getPageable(),
    // stocks.getTotalElements());
    // }

    public Page<StockResponseDTO> mapPageStockToPageStockResponseDTO(Page<Stock> stocks) {
        logger.info("mapPageStockToPageStockResponseDTO method started");
        Page<StockResponseDTO> res = stocks.map(stock -> mapper.map(stock, StockResponseDTO.class));
        logger.debug("Mapped {} stocks successfully mapped to DTO", stocks.getNumberOfElements());
        return res;
    }

    // public Stock mapStockRequestToStock(StockRequestDTO stock) {
    // Stock s = new Stock(stock.getSymbol());
    // return s;
    // }

    public PaginatedResponse<StockResponseDTO> mapPageToPaginatedResponse(Page<StockResponseDTO> page) {
        PaginatedResponse<StockResponseDTO> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setContent(page.getContent());
        paginatedResponse.setSize(page.getSize());
        paginatedResponse.setPage(page.getNumber());
        paginatedResponse.setTotalElements(page.getTotalElements());
        paginatedResponse.setTotalPages(page.getTotalPages());
        paginatedResponse.setLast(page.isLast());
        return paginatedResponse;
    }

}
