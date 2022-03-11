package com.soen487.t08.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;


@Component
@Path("")
public class InventoryRest {

    /**
     * Class for holding the list of inventory and handling the requests
     */

    private static ArrayList<InventoryItem> inventories = new ArrayList<>();

    /**
     * Meant for returning the list of inventory
     * @return A concatenation of the toString method for all inventory
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getInventory() {
        return inventories.stream().map(Object::toString).collect(Collectors.joining(".\n"));
    }

    /**
     * Meant for getting a inventory item with a specific ID
     * @param inventoryItemId of the inventory item
     * @return toString method of inventory item
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{inventoryItemId}")
    public String getInventoryList(@PathParam("inventoryItemId") int inventoryItemId) {
        InventoryItem inventoryItem = inventories.stream().filter(inventoryItem1 -> inventoryItem1.getInventoryItemId() == inventoryItemId)
                .findFirst()
                .orElse(null);
        if (inventoryItem != null) {
            return inventoryItem.toString();
        } else {
            return "Inventory item not found!";
        }
    }
    @GET
    @Produces("application/json")
    @Path("inventoryGetJson")
    public ArrayList<InventoryItem> getCatalogJson() {
        return inventories;
    }

    @GET
    @Path("inventoryGetCodeJson/{productCode}")
    @Produces("application/json")
    public InventoryItem getCatalogLisJson(@PathParam("productCode") String productCode) {
        return inventories.stream().filter(catalog1 -> catalog1.getProductCode().equals(productCode))
                .findFirst()
                .orElse(null);
    }
    /**
     * Meant for creating inventory using the post method
     * @param productCode of the inventory item
     * @param availableQuantity of the inventory item
     */
    @POST
    @Path("{productCode}/{availableQuantity}")
    public void createInventory(@PathParam("productCode") String productCode, @PathParam("availableQuantity") int availableQuantity) {
        InventoryItem newInventoryItem = new InventoryItem(productCode,availableQuantity);
        inventories.add(newInventoryItem);
    }

    /**
     * Meant for replacing inventory item with specific ID
     * @param inventoryItemId of the inventory item
     * @param productCode of the inventory item
     * @param availableQuantity of the inventory item
     */
    @PUT
    @Path("{inventoryItemId}/{productCode}/{availableQuantity}")
    public void modifyInventory(@PathParam("inventoryItemId") int inventoryItemId, @PathParam("productCode") String productCode, @PathParam("availableQuantity") int availableQuantity) {
        deleteInventoryItem(inventoryItemId);
        createInventory(productCode,availableQuantity);
    }

    /**
     * Meant for deleting inventory item with specific ID
     * @param inventoryItemId of the inventory item
     */
    @DELETE
    @Path("{inventoryItemId}")
    public void deleteInventoryItem(@PathParam("inventoryItemId") int inventoryItemId) {
        inventories = inventories.stream().filter(inventoryItem -> inventoryItem.getInventoryItemId() != inventoryItemId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Debugging statement that prints the current state of the list of inventory
     */
    private void printInventory() {
        for(InventoryItem inventoryItem : inventories) {
            System.out.println(inventoryItem);
        }
    }

}














