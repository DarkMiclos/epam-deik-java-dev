package com.epam.training.webshop.core.configuration;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grossprice.GrossPriceCalculator;
import com.epam.training.webshop.core.cart.grossprice.impl.GrossPriceCalculatorImpl;
import com.epam.training.webshop.core.cart.grossprice.impl.HungarianTaxGrossPriceDecorator;
import com.epam.training.webshop.core.finance.bank.Bank;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Bank bank() {
        return new Bank();
    }

    @Bean
    public Cart cart(Bank bank) {
        return Cart.createEmptyCart(bank);
    }

    @Bean
    public GrossPriceCalculator grossPriceCalculator() {
        return new HungarianTaxGrossPriceDecorator(new GrossPriceCalculatorImpl());
    }
}
