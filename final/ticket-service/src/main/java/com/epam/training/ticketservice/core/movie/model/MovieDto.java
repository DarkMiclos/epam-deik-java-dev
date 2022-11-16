package com.epam.training.ticketservice.core.movie.model;

import lombok.Value;

@Value
public class MovieDto {
    private final String name;
    private final String genre;
    private final Integer length;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private  String name;
        private  String genre;
        private  Integer length;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }
        
        public Builder withGenre(String genre) {
            this.genre = genre;
            return this;
        }
        
        public Builder withLength(Integer length) {
            this.length = length;
            return this;
        }

        public MovieDto build() {
            return new MovieDto(name, genre, length);
        }
    }
}
