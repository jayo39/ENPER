package com.jnjnetwork.CUHelper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String detail;

    @Column(nullable = false)
    private Long firstPage;

    @Column(nullable = false)
    private Long lastPage;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    private Book book;
}
