package com.jnjnetwork.CUHelper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Schedule {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private LocalTime endTime;

    private String note;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    private User user;

    public String getEndTime() {
        if (endTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return endTime.format(formatter);
        }
        return null;
    }

    private Boolean isFinished;
}
