package com.epam.training.ticketservice.core.pricecomponent.model;

import lombok.Value;

@Value
public class PriceComponentDto {
    private final String name;
    private final Integer amount;

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String name;
        private Integer amount;
        
        public Builder withName(String name) {
            this.name = name;
            return this;
        }
        
        public Builder withAmount(Integer amount) {
            this.amount = amount;
            return  this;
        }
        
        public PriceComponentDto build() {
            return new PriceComponentDto(name, amount);
        }
    }
}
