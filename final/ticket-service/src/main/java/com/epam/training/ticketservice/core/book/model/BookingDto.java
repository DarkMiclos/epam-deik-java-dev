package com.epam.training.ticketservice.core.book.model;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.seat.Seat;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.Value;

import java.util.List;

@Value
public class BookingDto {
    private final Screening screening;
    private final List<Seat> listOfSeats;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Screening screening;
        private List<Seat> listOfSeats;
        
        public Builder withScreening(Screening screening) {
            this.screening = screening;
            return  this;
        }
        
        public Builder withListOfSeat(List<Seat> listOfSeats) {
            this.listOfSeats = listOfSeats;
            return this;
        }
        
        public BookingDto build() {
            return new BookingDto(screening, listOfSeats);
        }
    }
}
