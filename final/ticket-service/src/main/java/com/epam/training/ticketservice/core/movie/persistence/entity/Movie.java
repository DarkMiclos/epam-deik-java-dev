package com.epam.training.ticketservice.core.movie.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String genre;
    private Integer lengthInMinutes;

    public Movie(String name, String genre, Integer lengthInMinutes) {
        this.name = name;
        this.genre = genre;
        this.lengthInMinutes = lengthInMinutes;
    }
}
