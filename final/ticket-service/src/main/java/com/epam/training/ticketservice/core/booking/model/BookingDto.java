package com.epam.training.ticketservice.core.booking.model;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.seat.Seat;
import lombok.Value;

import java.util.List;

@Value
public class BookingDto {
    private final Screening screening;
    private final List<Seat> listOfSeats;
    private final Integer price;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Screening screening;
        private List<Seat> listOfSeats;
        private Integer price;
        
        public Builder withScreening(Screening screening) {
            this.screening = screening;
            return  this;
        }
        
        public Builder withListOfSeat(List<Seat> listOfSeats) {
            this.listOfSeats = listOfSeats;
            return this;
        }
        
        public Builder withPrice(Integer price) {
            this.price = price;
            return this;
        }
        
        public BookingDto build() {
            return new BookingDto(screening, listOfSeats, price);
        }
    }
}
