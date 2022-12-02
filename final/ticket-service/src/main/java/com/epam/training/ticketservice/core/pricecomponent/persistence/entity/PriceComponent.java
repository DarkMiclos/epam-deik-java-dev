package com.epam.training.ticketservice.core.pricecomponent.persistence.entity;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class PriceComponent {
    @Id
    @GeneratedValue
    private Integer id;
    
    private String name;
    private Integer amount;
    
    @OneToMany(targetEntity = Room.class, fetch = FetchType.EAGER)
    private Set<Room> rooms;
    @OneToMany(targetEntity = Movie.class, fetch = FetchType.EAGER)
    private Set<Movie> movies;
    @OneToMany(targetEntity = Screening.class, fetch = FetchType.EAGER)
    private Set<Screening> screenings;

    public PriceComponent(String name, Integer amount) {
        this.name = name;
        this.amount = amount;
    }

    public PriceComponent(String name, Integer amount, Set<Room> rooms, Set<Movie> movies, Set<Screening> screenings) {
        this.name = name;
        this.amount = amount;
        this.rooms = rooms;
        this.movies = movies;
        this.screenings = screenings;
    }
}
