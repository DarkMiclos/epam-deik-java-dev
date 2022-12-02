package com.epam.training.ticketservice.core.seat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SeatTest {
    private static final Seat SEAT = new Seat(5,5);
    
    @Test
    void testEquals() {
        assertTrue(SEAT.equals(new Seat(5,5)));
        assertTrue(SEAT.equals(SEAT));
        assertFalse(SEAT.equals(new Seat(6,6)));
        assertFalse(SEAT.equals(null));
    }
}
