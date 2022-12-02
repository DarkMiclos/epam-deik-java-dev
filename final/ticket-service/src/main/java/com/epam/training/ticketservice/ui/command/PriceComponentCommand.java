package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.seat.Seat;
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
public class PriceComponentCommand {
    private final PriceComponentService priceComponentService;
    private final MovieService movieService;
    private final RoomService roomService;
    
    @ShellMethod(key = "update base price",
            value = "Updates the base price for a screening. By default this is 1500 HUF")
    public String updateBasePrice(String newBasePrice) {
        priceComponentService.updateBasePrice(Integer.parseInt(newBasePrice));
        return "The new base price is " + newBasePrice;
    }
    
    @ShellMethod(key = "create price component", value = "Creates a new price component. Admin command.")
    public String createPriceComponent(String name, String amount) {
        PriceComponentDto priceComponentDto = PriceComponentDto.builder()
                .withName(name)
                .withAmount(Integer.parseInt(amount))
                .build();
        priceComponentService.createPriceComponent(priceComponentDto);
        return "Price component created!";
    }
    
    @ShellMethod(key = "attach price component to room", value = "Attach a price component to a room. Admin command.")
    public String attachPriceComponentToRoom(String priceComponentName, String roomName) {
        priceComponentService.attachPriceComponentToRoom(priceComponentName, roomName);
        return "Price component attached to room!";
    }

    @ShellMethod(key = "attach price component to movie", value = "Attach a price component to a movie. Admin command.")
    public String attachPriceComponentToMovie(String priceComponentName, String movieName) {
        priceComponentService.attachPriceComponentToMovie(priceComponentName, movieName);
        return "Price component attached to movie!";
    }

    @ShellMethod(key = "attach price component to screening",
            value = "Attach a price component to a screening. Admin command.")
    public String attachPriceComponentToScreening(String priceComponentName,
                      String movieName, String roomName, String startDate) {
        try {
            Movie movie = movieService.getMovie(movieName);
            Room room = roomService.getRoom(roomName);
            ScreeningDto screeningDto = ScreeningDto.builder()
                    .withMovie(movie)
                    .withRoom(room)
                    .withBeginningDateOfScreening(convertStringToDate(startDate))
                    .build();
            priceComponentService.attachPriceComponentToScreening(priceComponentName, screeningDto);
            return "Price component attached to screening!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    @ShellMethod(key = "show price for", value = "Shows the price for a screening.")
    public String showPriceForScreening(String movieName, String roomName, String startDate, String listOfSeats) {
        try {
            Movie movie = movieService.getMovie(movieName);
            Room room = roomService.getRoom(roomName);
            ScreeningDto screeningDto = ScreeningDto.builder()
                    .withMovie(movie)
                    .withRoom(room)
                    .withBeginningDateOfScreening(convertStringToDate(startDate))
                    .build();
            int price = priceComponentService.showPriceForScreening(screeningDto);
            return "The price for this booking would be " 
                    + price * convertStringOfSeatsToList(listOfSeats).size() + " HUF";
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
