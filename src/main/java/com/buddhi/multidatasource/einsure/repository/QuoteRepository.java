package com.buddhi.multidatasource.einsure.repository;

import com.buddhi.multidatasource.einsure.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
