package com.lambdaschool.javaorders.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data @NoArgsConstructor
@Entity
@Table(name = "agents")
public class Agents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long agentcode;

    private String agentname, workingarea, phone, country;
    private double commission;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "agentcode")
    private Set<Customers> customers;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "agentcode")
    private Set<Orders> orders;
}
