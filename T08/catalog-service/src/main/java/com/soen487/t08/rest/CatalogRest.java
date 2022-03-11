package com.soen487.t08.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
@Path("product")
public class CatalogRest {
    /**
     * Class for holding the list of catalogs and handling the requests
     */
    private static ArrayList<Catalog> catalogs = new ArrayList<>();


    /**
     * Meant for returning the list of catalogs
     * @return A concatenation of the toString method for all catalogs
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCatalog() {
        return catalogs.stream().map(Object::toString).collect(Collectors.joining(".\n"));
    }
    /**
     * Meant for getting a catalog with a specific ID
     * @param id of the catalog
     * @return toString method of catalog
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{id}")
    public String getCatalogList(@PathParam("id") int id) {
        Catalog catalog = catalogs.stream().filter(catalog1 -> catalog1.getId() == id)
                .findFirst()
                .orElse(null);
        if (catalog != null) {
            return catalog.toString();
        } else {
            return "Catalog not found!";
        }
    }
    @GET
    @Produces("application/json")
    @Path("catalogGetJson")
    public ArrayList<Catalog> getCatalogJson() {
        return catalogs;
    }

    @GET
    @Path("catalogGetCodeJson/{productCode}")
    @Produces("application/json")
    public Catalog getCatalogLisJson(@PathParam("productCode") String productCode) {
        return catalogs.stream().filter(catalog1 -> catalog1.getProductCode().equals(productCode))
                .findFirst()
                .orElse(null);
    }

    /**
     * Meant for creating catalogs using the post method
     * @param code of the catalog
     * @param name of the catalog
     * @param description of the catalog
     * @param price of the catalog
     */

    @POST
    @Path("{code}/{name}/{description}/{price}")
    public void createCatalog(@PathParam("code") String code, @PathParam("name") String name,
                              @PathParam("description") String description, @PathParam("price") double price) {
        Catalog newCatalog = new Catalog(code,name, description,price);
        catalogs.add(newCatalog);
    }
    /**
     * Meant for replacing catalog with specific ID
     * @param id of the catalog
     * @param name of the catalog
     * @param code of the catalog
     * @param description of the catalog
     * @param price of the catalog
     */
    @PUT
    @Path("{id}/{code}/{name}/{description}/{price}")
    public void modifyCatalog(@PathParam("id") int id, @PathParam("code") String code, @PathParam("name") String name,
                              @PathParam("description") String description, @PathParam("price") double price) {
        deleteCatalog(id);
        createCatalog(code,name, description,price);
    }
    /**
     * Meant for deleting catalog with specific ID
     * @param id of the catalog
     */
    @DELETE
    @Path("{id}")
    public void deleteCatalog(@PathParam("id") int id) {
        catalogs = catalogs.stream().filter(catalog -> catalog.getId() != id)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    /**
     * Debugging statement that prints the current state of the list of catalogs
     */
    private void printCatalogs() {
        for(Catalog catalog : catalogs) {
            System.out.println(catalog);
        }
    }
}
