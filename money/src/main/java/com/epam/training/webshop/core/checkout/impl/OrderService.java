package com.epam.training.webshop.core.checkout.impl;

import com.epam.training.webshop.core.checkout.CheckoutObserver;
import com.epam.training.webshop.core.checkout.model.OrderDto;
import com.epam.training.webshop.core.checkout.persistence.model.Order;
import com.epam.training.webshop.core.checkout.persistence.model.OrderItem;
import com.epam.training.webshop.core.checkout.persistence.repository.OrderRepository;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import com.epam.training.webshop.core.user.UserService;
import com.epam.training.webshop.core.user.model.UserDto;
import com.epam.training.webshop.core.user.persistence.entity.User;
import com.epam.training.webshop.core.user.persistence.repository.UserRepository;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService implements CheckoutObserver {

    private final UserService userService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public void handleOrder(OrderDto orderDto) {
        UserDto userDto = userService.describe().orElseThrow(() -> new IllegalArgumentException("You need to first login!"));
        User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() -> new IllegalArgumentException("No such username!"));
        Order order = createOrderEntityFromDto(orderDto, user);
        orderRepository.save(order);
    }

    private Order createOrderEntityFromDto(OrderDto orderDto, User user) {
        return Order.builder()
            .user(user)
            .orderItems(orderDto.getProductDtoList().stream()
                .map(this::createOrderItemFromProduct)
                .collect(Collectors.toList()))
            .netPriceAmount(orderDto.getNetPrice().getAmount())
            .grossPriceAmount(orderDto.getGrossPrice().getAmount())
            .netPriceCurrencyCode(orderDto.getNetPrice().getCurrency().getCurrencyCode())
            .grossPriceCurrencyCode(orderDto.getGrossPrice().getCurrency().getCurrencyCode())
            .build();
    }

    public List<OrderDto> retrieveOrdersForUser(UserDto userDto) {
        return orderRepository.findByUserUsername(userDto.getUsername()).stream()
            .map(this::createOrderDtoFromEntity)
            .collect(Collectors.toList());
    }

    private OrderDto createOrderDtoFromEntity(Order order) {
        return new OrderDto(
            order.getOrderItems().stream().map(this::createProductFromOrderItem).collect(Collectors.toList()),
            new Money(order.getNetPriceAmount(), Currency.getInstance(order.getNetPriceCurrencyCode())),
            new Money(order.getGrossPriceAmount(), Currency.getInstance(order.getGrossPriceCurrencyCode()))
        );
    }

    private ProductDto createProductFromOrderItem(OrderItem orderItem) {
        return ProductDto.builder()
            .withName(orderItem.getName())
            .withNetPrice(new Money(orderItem.getNetPriceAmount(), Currency.getInstance(orderItem.getNetPriceCurrencyCode())))
            .build();
    }

    private OrderItem createOrderItemFromProduct(ProductDto productDto) {
        return OrderItem.builder()
            .name(productDto.getName())
            .netPriceAmount(productDto.getNetPrice().getAmount())
            .netPriceCurrencyCode(productDto.getNetPrice().getCurrency().getCurrencyCode())
            .build();
    }
}
