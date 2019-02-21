package com.lambdaschool.javaorders.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data @NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ordnum;

    private double ordamount, advanceamount;

    @ManyToOne
    @JoinColumn(name = "custcode", nullable = false)
//    @JsonIgnore
    private Customers customers;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "agentcode", nullable = false)
    private Agents agentcode;

    private String orddescription;

}
