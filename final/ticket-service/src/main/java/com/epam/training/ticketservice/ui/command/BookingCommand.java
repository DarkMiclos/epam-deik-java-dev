package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.seat.Seat;
import com.epam.training.ticketservice.core.seat.SeatRepository;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class BookingCommand {
    private final BookingService bookService;
    private final MovieService movieService;
    private final RoomService roomService;
    private final ScreeningService screeningService;
    private final PriceComponentService priceComponentService;
    private final SeatRepository seatRepository;
    private final UserService userService;
    private final Helper helper = new Helper();

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "book", value = "Books a ticket for an existing screening.")
    public String book(String movieName, String roomName, String startDate, String listOfSeats) {
        try {
            Screening screening = screeningService.getScreening(movieService.getMovie(movieName),
                    roomService.getRoom(roomName),
                    helper.convertStringToDate(startDate));
            List<Seat> seats = helper.convertStringOfSeatsToList(listOfSeats);
            seatRepository.saveAll(seats);
            BookingDto bookDto = BookingDto.builder()
                    .withScreening(screening)
                    .withListOfSeat(seats)
                    .withPrice(priceComponentService.getBasePrice())
                    .build();
            bookService.createBooking(bookDto);
            return "Seats booked: " + formatListOfSeatsForOutput(seats) 
                    + "; the price for this booking is "
                    + seats.size() * priceComponentService.getBasePrice() + " HUF";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    private String formatListOfSeatsForOutput(List<Seat> seats) {
        String output = "";
        for (var seat : seats) {
            output += seat.toString() + ", ";
        }
        output = output.substring(0, output.length() - 2);
        return output;
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().getRole() == User.Role.USER
                ? Availability.available()
                : Availability.unavailable("You are not a logged in regular user!");
    }
}
