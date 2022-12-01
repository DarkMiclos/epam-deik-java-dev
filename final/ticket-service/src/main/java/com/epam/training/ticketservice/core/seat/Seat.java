package com.epam.training.ticketservice.core.seat;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer rowNumber;
    private Integer columnNumber;

    public Seat(Integer row, Integer column) {
        this.rowNumber = row;
        this.columnNumber = column;
    }
    
    @Override
    public String toString() {
        return "(" + rowNumber + "," + columnNumber + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Seat seat = (Seat) o;
        return Objects.equals(rowNumber, seat.rowNumber) && Objects.equals(columnNumber, seat.columnNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNumber, columnNumber);
    }
}
