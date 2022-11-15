package com.epam.training.webshop.core.checkout.model;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import java.util.List;
import lombok.Value;

@Value
public class OrderDto {

    private final List<ProductDto> productDtoList;
    private final Money netPrice;
    private final Money grossPrice;
}
