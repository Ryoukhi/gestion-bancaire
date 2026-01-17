package com.eadl.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.eadl.backend.enums.TransactionType;

@Entity
@Table(name = "transactions")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    private LocalDateTime date;

    private BigDecimal balanceAfter;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}