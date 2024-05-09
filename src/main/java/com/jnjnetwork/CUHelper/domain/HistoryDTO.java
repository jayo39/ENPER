package com.jnjnetwork.CUHelper.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryDTO {
    @Getter
    private Long id;
    @Getter
    private String username;
    @Getter
    private String bookTitle;
    @Getter
    private String bookSeries;
    private LocalDateTime accessDate;

    public HistoryDTO(Long id, String username, String bookTitle, String bookSeries, LocalDateTime accessDate) {
        this.id = id;
        this.username = username;
        this.bookTitle = bookTitle;
        this.bookSeries = bookSeries;
        this.accessDate = accessDate;
    }

    public void setId(Long id) { this.id = id; }

    public void setUsername(String username) { this.username = username; }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBookSeries(String bookSeries) {
        this.bookSeries = bookSeries;
    }

    public String getAccessDate() {
        if (accessDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return accessDate.format(formatter);
        }
        return null;
    }

    public void setAccessDate(LocalDateTime accessDate) {
        this.accessDate = accessDate;
    }
}
