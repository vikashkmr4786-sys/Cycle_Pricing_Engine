package com.Hero.Pricing.repository;

import com.Hero.Pricing.entity.PartPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartPriceHistoryRepository extends JpaRepository<PartPriceHistory, Long>
{
    List<PartPriceHistory> findByPartIdOrderByUpdatedAtDesc(Long partId);
}
