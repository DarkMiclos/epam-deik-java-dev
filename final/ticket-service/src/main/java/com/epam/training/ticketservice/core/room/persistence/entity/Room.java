package com.epam.training.ticketservice.core.room.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Integer numberOfRows;
    private Integer numberOfColumns;

    public Room(String name, Integer numberOfRows, Integer numberOfColumns) {
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
    }
}
