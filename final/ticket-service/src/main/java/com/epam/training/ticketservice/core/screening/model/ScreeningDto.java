package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import lombok.Value;

import java.util.Date;

@Value
public class ScreeningDto {
    private final Movie movie;
    private final Room room;
    private final Date beginningDateOfScreening;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Movie movie;
        private Room room;
        private Date beginningDateOfScreening;
        
        public Builder withMovie(Movie movie) {
            this.movie = movie;
            return this;
        }
        
        public Builder withRoom(Room room) {
            this.room = room;
            return this;
        }
        
        public Builder withBeginningDateOfScreening(Date beginningDateOfScreening) {
            this.beginningDateOfScreening = beginningDateOfScreening;
            return this;
        }
        
        public ScreeningDto build() {
            return new ScreeningDto(movie, room, beginningDateOfScreening);
        }
    }
}
