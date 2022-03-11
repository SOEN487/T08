package com.soen487.t08.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;


@Component
@Path("")
public class OrderRest {
    private static int currentId = 1;
    /**
     * Class for holding the list of orders and handling the requests
     */
    private static Collection<Order> orders=new CopyOnWriteArrayList<>();
    /**
     * Meant for returning the list of orders
     * @return A concatenation of the toString method for all orders
     */
    @GET
    @Produces("application/xml")
    public String getOrders() {
        return orders.stream().map(Object::toString).collect(Collectors.joining(".\n"));
    }

       /**
     * Meant for getting a order with a specific ID
     * @param orderId of the order
     * @return toString method of order
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{orderId}")
    public String getOrderList(@PathParam("orderId") int orderId) {
        Order order = orders.stream().filter(order1 -> order1.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
        if (order != null) {
            return order.toString();
        } else {
            return "Order not found!";
        }
    }

    /**
     * Meant for creating orders using the post method
     * @param customerEmail of the order
     * @param customerAddress of the order
     * @param productName of the order
     * @param productCode of the order
     */
    @POST
    @Path("{customerEmail}/{customerAddress}/{productName}/{productCode}")
    public void createOrder(@PathParam("customerEmail") String customerEmail, @PathParam("customerAddress") String customerAddress,
                            @PathParam("productName") String productName, @PathParam("productCode") String productCode) {
        Order order = new Order(currentId++,customerEmail,customerAddress, productName,productCode);
        orders.add(order);
    }

    /**
     * Meant for replacing order with specific ID
     * @param orderId of the order
     * @param customerEmail of the order
     * @param customerAddress of the order
     * @param item of the order
     * @param productCode of the order
     */
    @PUT
    @Path("{orderId}/{customerEmail}/{customerAddress}/{item}/{productCode}")
    public void modifyOrder(@PathParam("orderId") int orderId, @PathParam("customerEmail") String customerEmail, @PathParam("customerAddress") String customerAddress,
                            @PathParam("item") String item,@PathParam("productCode") String productCode) {
        deleteOrder(orderId);
        createOrder(customerEmail,customerAddress,item,productCode);
    }

    /**
     * Meant for deleting order with specific ID
     * @param orderId of the order
     */
    @DELETE
    @Path("{orderId}")
    public void deleteOrder(@PathParam("orderId") int orderId) {
        orders = orders.stream().filter(order -> order.getOrderId() != orderId)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    @GET
    @Produces("application/xml")
    @Path("orderGetXML")
    public CopyOnWriteArrayList<Order> getOrderXML() {
        return (CopyOnWriteArrayList<Order>) orders;
    }
    @GET
    @Produces("application/json")
    @Path("orderGetJson")
    public CopyOnWriteArrayList<Order> getOrderJson() {
        return (CopyOnWriteArrayList<Order>) orders;
    }

    @GET
    @Path("orderGetIdJson/{orderId}")
    @Produces("application/json")
    public Order getOrderLisJson(@PathParam("orderId") int orderId) {
        return orders.stream().filter(order1 -> order1.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
    }
    /**
     * Meant for creating customers using the post method
     */
    @POST
    @Consumes("application/xml")
    @Path("orderPostXML")
    public void createOrder(Order order) {
        Order newOrder = new Order(order);
        orders.add(newOrder);
    }
    /**
     * Debugging statement that prints the current state of the list of orders
     */
    private void printOrders() {
        for(Order order : orders) {
            System.out.println(order);
        }
    }

}














