package com.epam.training.ticketservice.core.room.model;

import lombok.Value;

@Value
public class RoomDto {
    private final String name;
    private final Integer numberOfRows;
    private final Integer numberOfColumns;

    public Integer getNumberOfSeats() {
        return numberOfRows * numberOfColumns;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private Integer numberOfRows;
        private Integer numberOfColumns;

        public RoomDto.Builder withName(String name) {
            this.name = name;
            return this;
        }
        
        public RoomDto.Builder withNumberOfRows(Integer numberOfRows) {
            this.numberOfRows = numberOfRows;
            return this;
        }
        
        public RoomDto.Builder withNumberOfColumns(Integer numberOfColumns) {
            this.numberOfColumns = numberOfColumns;
            return this;
        }
        
        public RoomDto build() {
            return new RoomDto(name, numberOfRows, numberOfColumns);
        }
    }
}
