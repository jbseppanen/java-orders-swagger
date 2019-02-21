package com.lambdaschool.javaorders;

import com.lambdaschool.javaorders.models.Customers;
import com.lambdaschool.javaorders.models.Orders;
import com.lambdaschool.javaorders.models.Agents;
import com.lambdaschool.javaorders.repository.AgentsRepository;
import com.lambdaschool.javaorders.repository.CustomersRepository;
import com.lambdaschool.javaorders.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = {}, produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    @Autowired
    AgentsRepository agentRepo;

    @Autowired
    CustomersRepository custRepo;

    @Autowired
    OrdersRepository orderRepo;


    // ------------Customers Mapping------------

    @GetMapping("/customers")
    public List<Customers> allCustomers() {
        return custRepo.findAll();
    }

    @GetMapping("/customers/custcode/{custcode}")
    public Customers getCustomerByCustcode(@PathVariable long custcode) {
        Optional<Customers> customersById = custRepo.findById(custcode);
        if (customersById.isPresent()) {
            return customersById.get();
        } else {
            return null;
        }
    }

    @GetMapping("/customer/order")
    public List<Object[]> allCustomersWithOrders() {
        return orderRepo.customersWithOrders();
    }

    @GetMapping("/customer/name/{custname}")
    public List<Orders> getOrdersByCustName(@PathVariable String custname) {
        return orderRepo.findByCustomers_CustnameIgnoreCase(custname);
    }

    @GetMapping("/customer/order/{custcode}")
    public List<Orders> getOrdersByCustCode(@PathVariable long custcode) {
        return orderRepo.findByCustomers_Custcode(custcode);
    }

    @PostMapping("/customers")
    public Customers newCustomer(@RequestBody Customers newCustomer) throws URISyntaxException {
        return custRepo.save(newCustomer);
    }

    @PutMapping("/customers/custcode/{custcode}")
    public Customers updateCustomer(@RequestBody Customers customerToUpdate, @PathVariable long custcode) throws URISyntaxException {
        Optional<Customers> foundCustomer = custRepo.findById(custcode);
        if (foundCustomer.isPresent()) {
            customerToUpdate.setCustcode(custcode);
            custRepo.save(customerToUpdate);
            return customerToUpdate;
        } else {
            return null;
        }
    }

    @DeleteMapping("/customers/custcode/{custcode}")
    public Customers deleteCustomer(@PathVariable long custcode) {
        var customerToDelete = custRepo.findById(custcode);
        if (customerToDelete.isPresent()) {
            List<Orders> ordersFromCustomer = orderRepo.findByCustomers_Custcode(custcode);
            for (Orders order : ordersFromCustomer) {
                orderRepo.deleteById(order.getOrdnum());
            }
            custRepo.deleteById(custcode);
            return customerToDelete.get();
        } else {
            return null;
        }
    }

    // ------------Agents Mapping------------

    @GetMapping("/agents")
    public List<Agents> allAgents() {
        return agentRepo.findAll();
    }

    @GetMapping("/agents/agentcode/{agentcode}")
    public Agents getAgentById(@PathVariable long agentcode) {
        Optional<Agents> agentsById = agentRepo.findById(agentcode);
        if (agentsById.isPresent()) {
            return agentsById.get();
        } else {
            return null;
        }
    }

    @GetMapping("/agents/customers")
    public List<Object[]> allAgentsWithCustomers() {
        return agentRepo.agentsWithCustomers();
    }

    @GetMapping("/agents/orders")
    public List<Object[]> ordersWithAgents() {
        return orderRepo.ordersWithAgents();
    }

    @PostMapping("/agents")
    public Agents newAgent(@RequestBody Agents newAgent) throws URISyntaxException {
        return agentRepo.save(newAgent);
    }

    @PutMapping("/agents/agentcode/{agentcode}")
    public Agents updateAgent(@RequestBody Agents agentToUpdate, @PathVariable long agentcode) throws URISyntaxException {
        Optional<Agents> foundAgent = agentRepo.findById(agentcode);
        if (foundAgent.isPresent()) {
            agentToUpdate.setAgentcode(agentcode);
            agentRepo.save(agentToUpdate);
            return agentToUpdate;
        } else {
            return null;
        }
    }

    @DeleteMapping("/agents/agentcode/{agentcode}")
    public Agents deleteAgent(@PathVariable long agentcode) {
        var agentToDelete = agentRepo.findById(agentcode);
        if (agentToDelete.isPresent()) {
            int custSize = custRepo.findByAgentcode_Agentcode(agentcode).size();
            int ordersSize = orderRepo.findByAgentcode_Agentcode(agentcode).size();
            if (custSize == 0 && ordersSize == 0) {
                agentRepo.deleteById(agentcode);
                return agentToDelete.get();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // ------------Orders Mapping------------

    @GetMapping("/orders")
    public List<Orders> allOrders() {
        return orderRepo.findAll();
    }

    @GetMapping("/orders/ordnum/{ordnum}")
    public Orders getOrderById(@PathVariable long ordnum) {
        Optional<Orders> ordersById = orderRepo.findById(ordnum);
        if (ordersById.isPresent()) {
            return ordersById.get();
        } else {
            return null;
        }
    }

    @PostMapping("/orders")
    public Orders newOrder(@RequestBody Orders newOrder) throws URISyntaxException {
        return orderRepo.save(newOrder);
    }

    @PutMapping("/orders/ordnum/{ordnum}")
    public Orders updateOrder(@RequestBody Orders orderToUpdate, @PathVariable long ordnum) throws URISyntaxException {
        var foundOrder = orderRepo.findById(ordnum);
        if (foundOrder.isPresent()) {
            orderToUpdate.setOrdnum(ordnum);
            orderRepo.save(orderToUpdate);
            return orderToUpdate;
        } else {
            return null;
        }
    }

    @DeleteMapping("/orders/ordnum/{ordnum}")
    public void deleteOrder(@PathVariable long ordnum) {
        orderRepo.deleteById(ordnum);
    }
}
