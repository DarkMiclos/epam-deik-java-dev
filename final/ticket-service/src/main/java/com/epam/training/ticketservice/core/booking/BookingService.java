package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.user.model.UserDto;

import java.util.List;

public interface BookingService {
    void createBooking(BookingDto bookDto);
    
    List<BookingDto> getBookingsByUser(UserDto userDto);
}
