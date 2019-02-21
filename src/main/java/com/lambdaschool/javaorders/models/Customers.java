package com.lambdaschool.javaorders.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data @NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long custcode;

    @Column(nullable = false)
    private String custname;

    private String custcity, workingarea, custcountry, grade, phone;
    private double openingamt, receiveamt, paymentamt, outstandingamt;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "agentcode", nullable = false)
    private Agents agentcode;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "customers")
    private Set<Orders> orders;
}
