package com.epam.training.webshop.core.checkout.persistence.model;

import com.epam.training.webshop.core.user.persistence.entity.User;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private User user;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;
    private Double netPriceAmount;
    private String netPriceCurrencyCode;
    private Double grossPriceAmount;
    private String grossPriceCurrencyCode;
}
