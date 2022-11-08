package com.epam.training.webshop.core.cart;

import com.epam.training.webshop.core.finance.bank.Bank;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import java.util.Currency;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class CartTest {

    private static final Currency HUF_CURRENCY = Currency.getInstance("HUF");
    private static final Currency USD_CURRENCY = Currency.getInstance("USD");

    private final Bank mockBank = Mockito.mock(Bank.class);
    private final Cart underTest = Cart.createEmptyCart(mockBank);

    @Test
    void testAddProductShouldNotStoreTheProductWhenItIsNull() {
        // Given - When
        underTest.addProduct(null);

        // Then
        Assertions.assertTrue(underTest.getProductDtoList().isEmpty());
    }

    @Test
    void testAddProductShouldStoreTheProductWhenItIsNotNull() {
        // Given
        ProductDto productDto = new ProductDto("productName", new Money(1, USD_CURRENCY));
        Cart expected = Cart.createCart(mockBank, productDto);

        // When
        underTest.addProduct(productDto);

        // Then
        Assertions.assertFalse(underTest.getProductDtoList().isEmpty());
        Assertions.assertEquals(expected, underTest);
    }

    @Test
    void testGetAggregatedNetPriceShouldReturnZeroPriceWhenNoItemsAreInTheCart() {
        // Given
        Money expected = new Money(0, HUF_CURRENCY);

        // When
        Money result = underTest.getAggregatedNetPrice();

        //Then
        Assertions.assertEquals(expected, result);
        Mockito.verifyNoInteractions(mockBank);
    }

    @Test
    void testGetAggregatedNetPriceShouldReturnCorrectPriceWhenOneItemInTheCart() {
        // Given
        ProductDto productDto = new ProductDto("productName", new Money(1.0, USD_CURRENCY));
        underTest.addProduct(productDto);
        Money expected = new Money(2.1, HUF_CURRENCY);
        Mockito.when(mockBank.getExchangeRate(ArgumentMatchers.any())).thenReturn(Optional.of(2.1));

        // When
        Money result = underTest.getAggregatedNetPrice();

        // Then
        Assertions.assertEquals(expected, result);
        Mockito.verify(mockBank).getExchangeRate(ArgumentMatchers.any());
    }
}
