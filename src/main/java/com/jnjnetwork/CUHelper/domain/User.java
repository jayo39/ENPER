package com.jnjnetwork.CUHelper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Transient
    private String re_password;

    @Column
    private LocalDateTime log_date;

    // User:Role = N:M
    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @Builder.Default
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();
    public void addRole(Role... roles) {
        if(roles != null) {
            Collections.addAll(this.roles, roles);
        }
    }

    public String getFormattedLogDate() {
        if (log_date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return log_date.format(formatter);
        }
        return null;
    }

}
