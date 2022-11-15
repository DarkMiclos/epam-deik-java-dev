package com.epam.training.webshop.core.checkout.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grossprice.GrossPriceCalculator;
import com.epam.training.webshop.core.checkout.model.OrderDto;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {

    @Mock
    private GrossPriceCalculator grossPriceCalculator;

    @Mock
    private CheckoutObservable checkoutObservable;

    @InjectMocks
    private CheckoutServiceImpl underTest;

    @Test
    void testCheckoutShouldReturnWithAnOrderWhenCartIsNotNull() {
        // Given
        List<ProductDto> productDtoList = Collections.emptyList();
        Cart cart = mock(Cart.class);
        Money netPrice = new Money(1.0, Currency.getInstance("USD"));
        Money grossPrice = new Money(2.0, Currency.getInstance("USD"));
        when(cart.getProductDtoList()).thenReturn(productDtoList);
        when(cart.getAggregatedNetPrice()).thenReturn(netPrice);
        when(grossPriceCalculator.getAggregatedGrossPrice(cart)).thenReturn(grossPrice);
        OrderDto expected = new OrderDto(productDtoList, netPrice, grossPrice);

        // When
        OrderDto actual = underTest.checkout(cart);

        // Then
        assertEquals(expected, actual);
        verify(cart).getProductDtoList();
        verify(cart).getAggregatedNetPrice();
        verify(grossPriceCalculator).getAggregatedGrossPrice(cart);
        verify(checkoutObservable).notifyObservers(expected);
    }
}
