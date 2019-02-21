package com.lambdaschool.javaorders.repository;

import com.lambdaschool.javaorders.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByCustomers_CustnameIgnoreCase(String name);

    List<Orders> findByCustomers_Custcode(long custcode);

    List<Orders> findByAgentcode_Agentcode(long agentcode);

    @Query(value = "SELECT c.custname as Name , o.ordnum as Number, o.ordamount, o.advanceamount, o.agentcode, o.orddescription as Description FROM customers c, orders o WHERE c.custcode=o.custcode ORDER BY c.custname", nativeQuery = true)
    List<Object[]> customersWithOrders();

    @Query(value = "SELECT a.agentname, o.ordnum, o.orddescription FROM agents a, orders o WHERE a.agentcode=o.agentcode ORDER BY a.agentname", nativeQuery = true)
    List<Object[]> ordersWithAgents();
}
