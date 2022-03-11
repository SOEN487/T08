package com.soen487.t08.client;

import java.io.IOException;
import java.util.Scanner;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Sample Eureka client that discovers the inventory service using Eureka and sends requests.
 */
public class ExampleEurekaClient {

    private static ApplicationInfoManager applicationInfoManager;
    private static EurekaClient eurekaClient;
    public static String vipAddress = "inventory";

    private static synchronized ApplicationInfoManager initializeApplicationInfoManager(EurekaInstanceConfig instanceConfig) {
        if (applicationInfoManager == null) {
            InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
            applicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);
        }

        return applicationInfoManager;
    }

    private static synchronized EurekaClient initializeEurekaClient(ApplicationInfoManager applicationInfoManager, EurekaClientConfig clientConfig) {
        if (eurekaClient == null) {
            eurekaClient = new DiscoveryClient(applicationInfoManager, clientConfig);
        }
        return eurekaClient;
    }

    /**
     * Gets the list of inventory by calling the API
     * @return string representation of all the items currently in the inventory
     */
    private static String getInventory() {

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Creating the request to execute
            String url = getServiceURL() + "/inventoryGetJson";
            HttpGet httpget = new HttpGet(url);
            // Executing the request using the http client and obtaining the response
            CloseableHttpResponse response = client.execute(httpget);
            return readResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to get catalog";
        }
    }

    /**
     * Method for creating a inventory using the REST API
     * @param productCode of the inventory item
     * @param availableQuantity of the inventory item
     */
    private static void createInventory(String productCode, int availableQuantity) {
        String url = getServiceURL();
        String path=url+"/%s/%s";
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(String.format(path,
                    productCode,availableQuantity));
            CloseableHttpResponse httpresponse = client.execute(httpPost);
            httpresponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for getting the home page url of the eureka service, using vipAddress
     * @return string with service url
     */
    public static String getServiceURL(){
        InstanceInfo nextServerInfo = null;
        try {
            nextServerInfo = eurekaClient.getNextServerFromEureka(vipAddress, false);
        } catch (Exception e) {
            System.err.println("Cannot get an instance of example service to talk to from eureka");
            System.exit(-1);
        }
        return nextServerInfo.getHomePageUrl() + vipAddress;
    }

    /**
     * Reads the response and converts it into a string
     * @param response response from http request
     * @return string of the response
     * @throws IOException
     */
    public static String readResponse(CloseableHttpResponse response) throws IOException {
        // Handling the IO Stream from the response using scanner
        Scanner sc = new Scanner(response.getEntity().getContent());
        StringBuilder stringResponse = new StringBuilder();
        while (sc.hasNext()) {
            stringResponse.append(sc.nextLine());
            stringResponse.append("\n");
        }
        response.close();
        return stringResponse.toString();
    }
    /**
     * Returns an instance of the class RestTemplate, to be used for HTTP requests
     * @return instance of RestTemplate
     */
    public static void main(String[] args) {
        // create the client
        ApplicationInfoManager applicationInfoManager = initializeApplicationInfoManager(new MyDataCenterInstanceConfig());
        EurekaClient client = initializeEurekaClient(applicationInfoManager, new DefaultEurekaClientConfig());

        createInventory("TV-3001",100);
        createInventory("Sofa-2001",200);
        createInventory("Book-1001",50);

        System.out.println("\n"+"Get Inventory:"+"\n"+ getInventory());

        // shutdown the client
        eurekaClient.shutdown();
    }

}