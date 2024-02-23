package com.jnjnetwork.CUHelper.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String series;

    private String description;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String summary;

    @Column
    private Double book_level;

    @Column
    private Double ar_point;

    @OneToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<Detail> details = new ArrayList<>();

    @OneToOne
    @ToString.Exclude
    private Question question;

}
