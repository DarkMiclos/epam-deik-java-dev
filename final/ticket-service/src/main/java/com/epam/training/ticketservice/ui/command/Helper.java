package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.seat.Seat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Helper {
    public Date convertStringToDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .parse(date);
    }

    public String convertDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(date);
    }

    public List<Seat> convertStringOfSeatsToList(String listOfSeats) {
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
}
