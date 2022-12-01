package com.epam.training.ticketservice.core.book.persistence.entity;


import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.seat.Seat;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
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

    public Booking(Screening screening, List<Seat> listOfSeats, User user) {
        this.screening = screening;
        this.listOfSeats = listOfSeats;
        this.user = user;
    }
}
