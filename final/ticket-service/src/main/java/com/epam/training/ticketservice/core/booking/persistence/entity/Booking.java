package com.epam.training.ticketservice.core.booking.persistence.entity;


import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.seat.Seat;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Screening screening;
    @OneToMany(targetEntity = Seat.class, fetch = FetchType.EAGER)
    private List<Seat> listOfSeats;
    @ManyToOne
    private User user;
    private Integer price;

    public Booking(Screening screening, List<Seat> listOfSeats, User user, Integer price) {
        this.screening = screening;
        this.listOfSeats = listOfSeats;
        this.user = user;
        this.price = price;
    }
}
