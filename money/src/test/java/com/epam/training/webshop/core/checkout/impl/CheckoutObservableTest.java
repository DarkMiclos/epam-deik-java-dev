package com.epam.training.webshop.core.checkout.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.epam.training.webshop.core.checkout.CheckoutObserver;
import com.epam.training.webshop.core.checkout.model.OrderDto;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;

class CheckoutObservableTest {

    @Test
    void testNotifyObserversShouldCallAllObserverWhenOrderIsPassed() {
        // Given
        List<ProductDto> productDtoList = Collections.emptyList();
        Money netPrice = new Money(1.0, Currency.getInstance("USD"));
        Money grossPrice = new Money(2.0, Currency.getInstance("USD"));
        OrderDto orderDto = new OrderDto(productDtoList, netPrice, grossPrice);
        CheckoutObserver checkoutObserver = mock(CheckoutObserver.class);
        CheckoutObservable underTest = new CheckoutObservable(List.of(checkoutObserver));

        // When
        underTest.notifyObservers(orderDto);

        // Then
        verify(checkoutObserver).handleOrder(orderDto);
        verifyNoMoreInteractions(checkoutObserver);
    }
}
