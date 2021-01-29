package com.guavapay.task.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "card")
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "card_period")
    private Integer cardPeriod;

    @Column(name = "urgent")
    private Boolean urgent;

    @Column(name = "code_word")
    private String codeWord;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "account_number")
    private String accountNumber;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "card")
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    @JsonBackReference
    private CardType cardType;
}