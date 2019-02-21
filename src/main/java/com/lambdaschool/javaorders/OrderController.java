package com.lambdaschool.javaorders;

import com.lambdaschool.javaorders.models.Agents;
import com.lambdaschool.javaorders.models.Customers;
import com.lambdaschool.javaorders.models.Orders;
import com.lambdaschool.javaorders.repository.AgentsRepository;
import com.lambdaschool.javaorders.repository.CustomersRepository;
import com.lambdaschool.javaorders.repository.OrdersRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Api(value = "Java Orders Application", description = "Java Orders with Swagger")
@ApiResponses(value =
        {
                @ApiResponse(code = 200, message = "Success!"),
                @ApiResponse(code = 201, message = "Item created/updated"),
                @ApiResponse(code = 401, message = "You are not authorized to the view this resource."),
                @ApiResponse(code = 403, message = "Accessing this resource is forbidden."),
                @ApiResponse(code = 404, message = "The resource you were trying to reach is not found.")
        })
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
@ApiOperation(value = "Returns a list of all customers.", response = List.class)
    @GetMapping("/customers")
    public List<Customers> allCustomers() {
        return custRepo.findAll();
    }

    @ApiOperation(value = "Returns a single customer based on supplied customer id (custcode).", response = Customers.class)
    @GetMapping("/customers/custcode/{custcode}")
    public Customers getCustomerByCustcode(@PathVariable long custcode) {
        Optional<Customers> customersById = custRepo.findById(custcode);
        if (customersById.isPresent()) {
            return customersById.get();
        } else {
            return null;
        }
    }

    @ApiOperation(value = "Returns a list of all Customers (denoted by custcode) and the orders associated with each customer.", response = List.class)
    @GetMapping("/customer/order")
    public List<Object[]> allCustomersWithOrders() {
        return orderRepo.customersWithOrders();
    }

    @ApiOperation(value = "Returns a list of orders associated with a single customer (denoted by the supplied customer name{custname}).", response = List.class)
    @GetMapping("/customer/name/{custname}")
    public List<Orders> getOrdersByCustName(@PathVariable String custname) {
        return orderRepo.findByCustomers_CustnameIgnoreCase(custname);
    }

    @ApiOperation(value = "Returns a list of orders associated with a single customer (denoted by the supplied customer id {custcode}).", response = List.class)
    @GetMapping("/customer/order/{custcode}")
    public List<Orders> getOrdersByCustCode(@PathVariable long custcode) {
        return orderRepo.findByCustomers_Custcode(custcode);
    }

    @ApiOperation(value = "Saves a single supplied customer to the database and returns that customer.", response = Customers.class)
    @PostMapping("/customers")
    public Customers newCustomer(@RequestBody Customers newCustomer) throws URISyntaxException {
        return custRepo.save(newCustomer);
    }

    @ApiOperation(value = "Updates a single supplied customer based on supplied customer id (custcode) to the database " +
            "and returns that customer if it is found.  If not found in database, returns null", response = Customers.class)
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

    @ApiOperation(value = "Deletes a single customer from database based on supplied customer id {custcode} and returns" +
            " that customer if found.  Any orders associated with the customer are deleted as well.  If not found, returns null.", response = Customers.class)
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

    @ApiOperation(value = "Returns a list of all agents.", response = List.class)
    @GetMapping("/agents")
    public List<Agents> allAgents() {
        return agentRepo.findAll();
    }

    @ApiOperation(value = "Returns a single agent based on supplied agent id (agentcode).", response = Agents.class)
    @GetMapping("/agents/agentcode/{agentcode}")
    public Agents getAgentById(@PathVariable long agentcode) {
        Optional<Agents> agentsById = agentRepo.findById(agentcode);
        if (agentsById.isPresent()) {
            return agentsById.get();
        } else {
            return null;
        }
    }

    @ApiOperation(value = "Returns a list of all agents (denoted by agent id{agentcode}) with the customers associated with each agent.", response = List.class)
    @GetMapping("/agents/customers")
    public List<Object[]> allAgentsWithCustomers() {
        return agentRepo.agentsWithCustomers();
    }

    @ApiOperation(value = "Returns a list of agents (denoted by agent id{agentcode}) with the orders associated each agent.", response = List.class)
    @GetMapping("/agents/orders")
    public List<Object[]> ordersWithAgents() {
        return orderRepo.ordersWithAgents();
    }

    @ApiOperation(value = "Saves a single supplied agent to the database and returns that agent.", response = Agents.class)
    @PostMapping("/agents")
    public Agents newAgent(@RequestBody Agents newAgent) throws URISyntaxException {
        return agentRepo.save(newAgent);
    }

    @ApiOperation(value = "Updates a single supplied agent based on supplied agent id (agentcode) to the database " +
            "and returns that agent if it is found.  If not found in database, returns null", response = Agents.class)
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

    @ApiOperation(value = "Deletes a single agent from database based on supplied agent id {agentcode} if found and if " +
            "there are no customers or orders associated with the agent.  If not found or has customers/orders connected to it, returns null.", response = Customers.class)
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

    @ApiOperation(value = "Returns a list of all orders.", response = List.class)
    @GetMapping("/orders")
    public List<Orders> allOrders() {
        return orderRepo.findAll();
    }

    @ApiOperation(value = "Returns a single order based on supplied order id (ordnum).", response = Orders.class)
    @GetMapping("/orders/ordnum/{ordnum}")
    public Orders getOrderById(@PathVariable long ordnum) {
        Optional<Orders> ordersById = orderRepo.findById(ordnum);
        if (ordersById.isPresent()) {
            return ordersById.get();
        } else {
            return null;
        }
    }

    @ApiOperation(value = "Saves a single supplied order to the database and returns that order.", response = Agents.class)
    @PostMapping("/orders")
    public Orders newOrder(@RequestBody Orders newOrder) throws URISyntaxException {
        return orderRepo.save(newOrder);
    }

    @ApiOperation(value = "Updates a single supplied order based on supplied order id (ordnum) to the database " +
            "and returns that order if it is found.  If not found in database, returns null", response = Agents.class)
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

    @ApiOperation(value = "Deletes a single order from database based on supplied order id {ordnum} and returns" +
            " that order if found.  If not found, returns null.", response = Customers.class)
    @DeleteMapping("/orders/ordnum/{ordnum}")
    public void deleteOrder(@PathVariable long ordnum) {
        orderRepo.deleteById(ordnum);
    }
}
