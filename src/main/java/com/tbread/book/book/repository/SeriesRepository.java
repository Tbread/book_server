package com.tbread.book.book.repository;

import com.tbread.book.book.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series,Long> {
    Optional<Series> findById(long id);
    Optional<Series> findBySeriesName(String seriesName);
}
