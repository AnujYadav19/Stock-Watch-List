package com.anuj.stockwatchlist.repository;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anuj.stockwatchlist.models.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Stock findBySymbol(String symbol);

    Page<Stock> findBySymbolContaining(String symbol, Pageable pageable);

}
