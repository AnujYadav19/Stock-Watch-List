package com.anuj.stockwatchlist.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anuj.stockwatchlist.models.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Optional<List<Stock>> findBySymbolAndUserId(String symbol, int userId);
    // Page<Stock> findBySymbolContaining(String symbol, Pageable pageable);

    List<Stock> findByUserId(int id);

    Page<Stock> findByUserIdAndSymbolContaining(int userId, String symbol, Pageable pageable);

    Page<Stock> findByUserId(int userId, Pageable pageable);

}
