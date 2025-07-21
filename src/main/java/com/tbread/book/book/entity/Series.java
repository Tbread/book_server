package com.tbread.book.book.entity;

import com.tbread.book.common.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Series extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seriesName;

    @Column(nullable = false)
    private boolean disable;

    public Series(String seriesName) {
        this.seriesName = seriesName;
        this.disable = false;
    }

    public void disableSeries() {
        this.disable = true;
    }

    public void enableSeries() {
        this.disable = false;
    }
}
