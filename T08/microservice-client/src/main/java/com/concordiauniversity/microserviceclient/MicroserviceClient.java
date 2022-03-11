package com.concordiauniversity.microserviceclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;

public class MicroserviceClient {
    public static String productCode;
    private static void getOrders() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Creating the request to execute
            String url="http://localhost:9191/order/orderGetIdJson/1";
            HttpGet httpget = new HttpGet(url);
            // Executing the request using the http client and obtaining the response
            CloseableHttpResponse response = client.execute(httpget);
            String responseJsonString=restTemplate().getForEntity(url ,String.class).getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValueAsString(responseJsonString);
            JsonNode jsonNode = objectMapper.readTree(responseJsonString);
            System.out.println("\n"+"Order Information:");
            System.out.println("\n"+"Order ID: "+jsonNode.get("orderId"));
            System.out.println("\n"+"CustomerEmail: "+jsonNode.get("customerEmail"));
            System.out.println("\n"+"CustomerAddress: "+jsonNode.get("customerAddress"));
            System.out.println("\n"+"productName"+jsonNode.get("productName"));
            System.out.println("\n"+"Product Code: "+jsonNode.get("productCode"));
            productCode=jsonNode.get("productCode").asText();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to get order");
        }
    }
    private static void getCatalogs() throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Creating the request to execute
            String url="http://localhost:9191/catalog/product/catalogGetCodeJson/"+productCode;
            HttpGet httpget = new HttpGet(url);
            // Executing the request using the http client and obtaining the response
            CloseableHttpResponse response = client.execute(httpget);
            String responseJsonString=restTemplate().getForEntity(url ,String.class).getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValueAsString(responseJsonString);
            JsonNode jsonNode = objectMapper.readTree(responseJsonString);
            System.out.println("\n"+"Catalog Information:");
            System.out.println("\n"+"productName: "+jsonNode.get("productName"));
            System.out.println("\n"+"description: "+jsonNode.get("description"));
            System.out.println("\n"+"price: "+jsonNode.get("price"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("\n"+"Failed to get catalog");
        }
    }


    private static void getInventory() throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Creating the request to execute
            String url="http://localhost:9191/inventory/inventoryGetCodeJson/"+productCode;
            HttpGet httpget = new HttpGet(url);
            // Executing the request using the http client and obtaining the response
            CloseableHttpResponse response = client.execute(httpget);
            String responseJsonString=restTemplate().getForEntity(url ,String.class).getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValueAsString(responseJsonString);
            JsonNode jsonNode = objectMapper.readTree(responseJsonString);
            System.out.println("\n"+"Inventory Information:");
            System.out.println("\n"+"Available Quantity: "+jsonNode.get("availableQuantity"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("\n"+"Failed to get inventory information");
        }
    }
@Bean
static RestTemplate restTemplate() {
    return new RestTemplate();
}
    public static void main(String[] args) throws IOException {
        getOrders();
        getCatalogs();
        getInventory();
    }

}
