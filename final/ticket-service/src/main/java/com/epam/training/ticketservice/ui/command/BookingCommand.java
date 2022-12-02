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
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ShellComponent
@AllArgsConstructor
public class BookingCommand {
    private final BookingService bookService;
    private final MovieService movieService;
    private final RoomService roomService;
    private final ScreeningService screeningService;
    private final PriceComponentService priceComponentService;
    private final SeatRepository seatRepository;
    
    @ShellMethod(key = "book", value = "Books a ticket for an existing screening.")
    public String book(String movieName, String roomName, String startDate, String listOfSeats) {
        try {
            Screening screening = screeningService.getScreening(movieService.getMovie(movieName),
                    roomService.getRoom(roomName),
                    convertStringToDate(startDate));
            List<Seat> seats = convertStringOfSeatsToList(listOfSeats);
            seatRepository.saveAll(seats);
            BookingDto bookDto = BookingDto.builder()
                    .withScreening(screening)
                    .withListOfSeat(seats)
                    .build();
            bookService.createBooking(bookDto);
            return "Seats booked: " + formatListOfSeatsForOutput(seats) 
                    + "; the price for this booking is "
                    + seats.size() * priceComponentService.getBasePrice() + " HUF";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private Date convertStringToDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .parse(date);
    }
    
    private List<Seat> convertStringOfSeatsToList(String listOfSeats) {
        List<String> tmp =  Arrays.asList(listOfSeats.replace("(", "")
                .replace(")", "")
                .split(" "));
        List<Seat> result = new ArrayList<>();
        for (var string : tmp) {
            List<String> rowAndColumnPair = Arrays.asList(string.split(","));
            Seat seat = new Seat(Integer.parseInt(rowAndColumnPair.get(0)),
                    Integer.parseInt(rowAndColumnPair.get(1)));
            result.add(seat);
        }
        return result;
    }
    
    private String formatListOfSeatsForOutput(List<Seat> seats) {
        String output = "";
        for (var seat : seats) {
            output += seat.toString() + ", ";
        }
        output = output.substring(0, output.length() - 2);
        return output;
    }
}
