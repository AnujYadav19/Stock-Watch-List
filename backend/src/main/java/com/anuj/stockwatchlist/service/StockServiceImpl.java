package com.anuj.stockwatchlist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.anuj.stockwatchlist.exceptions.UnauthorizedException;
import com.anuj.stockwatchlist.models.Stock;
import com.anuj.stockwatchlist.models.User;
import com.anuj.stockwatchlist.repository.StockRepository;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    @Override
    public StockResponseDTO saveStock(StockRequestDTO s, int user_id) {

        User user = userService.findUserById(user_id);

        logger.info("saveStock method started with symbol: {}", s.getSymbol());
        // Stock theStock = mapStockRequestToStock(s);
        Stock theStock = mapper.map(s, Stock.class);
        // return mapStockToStockResponseDTO(stockRepository.save(theStock));
        logger.debug("Stock mapped to entity: {} ", theStock);

        theStock.setUser(user);
        StockResponseDTO savedStock = mapper.map(stockRepository.save(theStock), StockResponseDTO.class);
        logger.info("Stock saved successfully with id:{} ", theStock);
        savedStock.setUsername(user.getUsername());
        return savedStock;
    }

    @Override
    public StockResponseDTO deleteStockById(int id, int user_id) {

        Stock theStock = findById(id, user_id);
        User theUser = theStock.getUser();

        stockRepository.deleteById(id);
        StockResponseDTO s = mapper.map(theStock, StockResponseDTO.class);
        s.setUsername(theUser.getUsername());
        return s;
    }

    @Override
    public StockResponseDTO findByIdDTO(int id, int user_id) {
        logger.info("findByIdDTO method started with id : {}", id);

        // This ONE line fetches, checks if it exists, and verifies ownership!
        Stock stock = findById(id, user_id);

        StockResponseDTO dto = mapper.map(stock, StockResponseDTO.class);
        dto.setUsername(stock.getUser().getUsername());
        logger.debug("Stock mapped to response dto for id: {}", id);

        return dto;
    }

    @Override
    public Stock findById(int id, int user_id) {

        logger.info("findById method started with id:{}", id);
        Optional<Stock> s = stockRepository.findById(id);
        if (s.isEmpty()) {
            logger.warn("Stock not found with id:{}", id);
            throw new StockNotFoundException("No stock with this id: " + id);
        }
        if (user_id != s.get().getUser().getId()) {
            throw new UnauthorizedException("This stock does not belong to the current user");
        }
        logger.info("Stock retrieved successfully with id: {}", id);
        Stock stock = s.get();
        return stock;

    }

    @Override
    public StockResponseDTO updateStockById(int id, StockRequestDTO s, int user_id) {

        logger.info("updateStockById method started with id: {}", id);
        Stock theStock = findById(id, user_id);
        logger.info("Stock retrieved successfully with id:{}", id);

        // ?this is not need
        // ?since findById already does the ownership check
        // if (user_id != theStock.getUser().getId()) {
        // throw new UnauthorizedException("This stock does not belong to the current
        // user");
        // }

        theStock.setSymbol(s.getSymbol());
        theStock.setBuyPrice(s.getBuyPrice());
        theStock.setQuantity(s.getQuantity());
        logger.debug("Stock fields updated for id: {}", id);

        Stock updatedStock = stockRepository.save(theStock);
        logger.info("Stock updated successfully with id: {}", id);

        // return mapStockToStockResponseDTO(updatedStock);
        StockResponseDTO mappedStock = mapper.map(updatedStock, StockResponseDTO.class);
        mappedStock.setUsername(theStock.getUser().getUsername());
        logger.debug("Stock entity successfully mapped to DTO for id: {}", id);
        return mappedStock;
    }

    @Override
    public PaginatedResponse<StockResponseDTO> getStocks(String symbol, int page, int size, String[] sortBy,
            String direction[], int user_id) {
        logger.info("getStocks method started with symbol: {}", symbol);
        Set<String> validFields = Set.of("symbol", "id", "buyprice", "quantity");
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

            stocks = stockRepository.findByUserIdAndSymbolContaining(user_id, symbol, pageable);

        } else {
            logger.info("Fetching all stocks");
            stocks = stockRepository.findByUserId(user_id, pageable);
        }

        logger.info("Fetched {} stocks successfully", stocks.getNumberOfElements());

        Page<StockResponseDTO> pageList = mapPageStockToPageStockResponseDTO(stocks);
        return mapPageToPaginatedResponse(pageList);
    }

    @Override
    public List<StockResponseDTO> findBySymbol(String symbol, int user_id) {
        logger.info("findBySymbol method started with symbol : {}", symbol);
        Optional<List<Stock>> stocks = stockRepository.findBySymbolAndUserId(symbol, user_id);
        if (stocks.isEmpty()) {
            logger.warn("No stock found with symbol: {}", symbol);
            throw new StockNotFoundException("No stock with the symbol: " + symbol);
        }
        // no need for id check as the method itself takes the id
        logger.info("Stock retrieved successfully with symbol: {}", symbol);
        List<StockResponseDTO> listOfStocks = stocks.get().stream().map((Stock s) -> {
            StockResponseDTO dto = mapper.map(s, StockResponseDTO.class);
            dto.setUsername(s.getUser().getUsername());
            return dto;
        }).collect(Collectors.toList());
        logger.debug("Stock mapped to DTO for symbol: {}", symbol);
        return listOfStocks;
    }

    public Page<StockResponseDTO> mapPageStockToPageStockResponseDTO(Page<Stock> stocks) {
        logger.info("mapPageStockToPageStockResponseDTO method started");
        Page<StockResponseDTO> res = stocks.map(stock -> {
            // 1. Map the standard fields
            StockResponseDTO dto = mapper.map(stock, StockResponseDTO.class);

            // 2. Reach into the User entity inside the Stock and pull the username
            dto.setUsername(stock.getUser().getUsername());

            return dto;
        });
        logger.debug("Mapped {} stocks successfully mapped to DTO", stocks.getNumberOfElements());
        return res;
    }

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
