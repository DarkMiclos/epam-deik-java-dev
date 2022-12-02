package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    
    @Override
    public void createBooking(BookingDto bookDto) {
        checkIfSeatExists(bookDto);
        checkIfSeatIsTaken(bookDto);
        Optional<User> user = userRepository.findByUsername(userService.describe().get().getUsername());
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovie(bookDto.getScreening().getMovie())
                .withRoom(bookDto.getScreening().getRoom())
                .withBeginningDateOfScreening(bookDto.getScreening().getBeginningDateOfScreening())
                .build();
        Booking book = new Booking(bookDto.getScreening(),
                bookDto.getListOfSeats(),
                user.get(),
                bookDto.getPrice());
        bookRepository.save(book);
    }

    @Override
    public List<BookingDto> getBookingsByUser(UserDto userDto) {
        List<Booking> bookings = bookRepository.findByUser_Username(userDto.getUsername());
        List<BookingDto> bookingDtoList = bookings.stream()
                .map(this::convertFromEntityToDto)
                .collect(Collectors.toList());
        return bookingDtoList;
    }

    private void checkIfSeatExists(BookingDto bookDto) {
        int numberOfRows = bookDto.getScreening()
                .getRoom()
                .getNumberOfRows();
        int numberOfColumns = bookDto.getScreening()
                .getRoom()
                .getNumberOfColumns();
        for (var seat : bookDto.getListOfSeats()) {
            if (seat.getRowNumber() > numberOfRows 
                    || seat.getColumnNumber() > numberOfColumns) {
                throw new IllegalArgumentException("Seat "
                        + seat.toString() + " does not exist in this room");
            }
        }
    }
    
    private void checkIfSeatIsTaken(BookingDto bookDto) {
        List<Booking> bookingListOfRoom = bookRepository.findByScreening_Room(bookDto.getScreening().getRoom());
        for (var booking : bookingListOfRoom) {
            for (var seat : booking.getListOfSeats()) {
                for (var currentSeat : bookDto.getListOfSeats()) {
                    if (currentSeat.equals(seat)) {
                        throw new IllegalArgumentException("Seat "
                                + seat.toString() + " is already taken");
                    }
                }
            }
        }
    }

    private BookingDto convertFromEntityToDto(Booking booking) {
        return new BookingDto(booking.getScreening(),
                booking.getListOfSeats(),
                booking.getPrice());
    }
}
