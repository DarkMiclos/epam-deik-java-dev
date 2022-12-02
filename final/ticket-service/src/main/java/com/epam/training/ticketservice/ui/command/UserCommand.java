package com.epam.training.ticketservice.ui.command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class UserCommand {

    private final UserService userService;
    
    private final BookingService bookingService;
    
    private final PriceComponentService priceComponentService;

    @ShellMethod(key = "sign out", value = "User logout")
    public String signOut() {
        Optional<UserDto> user = userService.signOut();
        if (user.isEmpty()) {
            return "You need to login first!";
        }
        return user.get() + " is logged out!";
    }

    @ShellMethod(key = "sign in privileged", value = "Admin login")
    public String signInPrivileged(String username, String password) {
        Optional<UserDto> user = userService.signIn(username, password, true);
        if (user.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        return user.get() + " is successfully logged in!";
    }
    
    @ShellMethod(key = "sign in", value = "User login")
    public String signIn(String userName, String password) {
        Optional<UserDto> user = userService.signIn(userName, password, false);
        if (user.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        return user.get() + " is successfully logged in!";
    }

    @ShellMethod(key = "describe account", value = "Get admin account information")
    public String describe() {
        Optional<UserDto> user = userService.describe();
        if (user.isEmpty()) {
            return "You are not signed in";
        }
        if (user.get().getRole().equals(User.Role.ADMIN)) {
            return "Signed in with privileged account '".concat(user.get().getUsername()).concat("'");
        }
        List<BookingDto> bookingDtoList = bookingService.getBookingsByUser(user.get());
        if (bookingDtoList.isEmpty()) {
            return "Signed in with account '" + user.get().getUsername() + "'\n"
                    + "You have not booked any tickets yet";
        }
        return "Signed in with account '" + user.get().getUsername()
                + "'\nYour previous bookings are\n"
                + createBookingListOutput(bookingDtoList);
    }

    @ShellMethod(key = "sign up", value = "User registration")
    public String signUp(String userName, String password) {
        try {
            userService.signUp(userName, password);
            return "Registration was successful!";
        } catch (Exception e) {
            return "Registration failed!";
        }
    }
    
    private String createBookingListOutput(List<BookingDto> bookingDtoList) {
        String output = "";
        for (var booking : bookingDtoList) {
            output += "Seats ";
            for (var seat : booking.getListOfSeats()) {
                output += seat.toString() + ", ";
            }
            output = output.substring(0, output.length() - 2);
            output += " on " + booking.getScreening().getMovie().getName() + " ";
            output += "in room " + booking.getScreening().getRoom().getName() + " ";
            output += "starting at " 
                    + convertDateToString(booking.getScreening().getBeginningDateOfScreening()) 
                    + " for " + booking.getListOfSeats().size() * priceComponentService.getBasePrice() + " HUF";//Todo create price for booking
        }
        return output;
    }

    private String convertDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(date);
    }
}
