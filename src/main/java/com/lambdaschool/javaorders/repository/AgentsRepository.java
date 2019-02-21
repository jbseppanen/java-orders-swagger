package com.lambdaschool.javaorders.repository;

import com.lambdaschool.javaorders.models.Agents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgentsRepository extends JpaRepository<Agents, Long> {
    @Query(value = "SELECT a.agentcode, a.agentname, a.workingarea as agentarea, a.commission, a.phone as agentphone, a.country,  c.custcode, c.custname, c.custcity, c.workingarea as customerarea, c.custcountry, c.grade, c.openingamt, c.receiveamt, c.paymentamt, c.outstandingamt, c.phone as customerphone FROM customers c, agents a WHERE c.agentcode=a.agentcode ORDER BY a.agentname", nativeQuery = true)
    List<Object[]> agentsWithCustomers();

    @Query(value = "SELECT * FROM agents ORDER BY agentname", nativeQuery = true)
    List<Object[]> allAgents();
}