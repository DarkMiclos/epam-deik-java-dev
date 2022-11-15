package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.checkout.impl.OrderService;
import com.epam.training.webshop.core.checkout.model.OrderDto;
import com.epam.training.webshop.core.user.UserService;
import com.epam.training.webshop.core.user.model.UserDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@AllArgsConstructor
public class OrderCommand {

    private final OrderService orderService;
    private final UserService userService;

    @ShellMethodAvailability("isLoggedIn")
    @ShellMethod(key = "user order list", value = "List previous orders")
    public List<OrderDto> listOrders() {
        UserDto loggedInUser = userService.describe().get();
        return orderService.retrieveOrdersForUser(loggedInUser);
    }

    private Availability isLoggedIn() {
        return userService.describe().isPresent()
            ? Availability.available()
            : Availability.unavailable("You are not logged in!");
    }
}
