package com.epam.training.ticketservice.core.book;

import com.epam.training.ticketservice.core.book.model.BookingDto;
import com.epam.training.ticketservice.core.user.model.UserDto;

import java.util.List;

public interface BookingService {
    void createBooking(BookingDto bookDto);
    
    List<BookingDto> getBookingsByUser(UserDto userDto);
}
