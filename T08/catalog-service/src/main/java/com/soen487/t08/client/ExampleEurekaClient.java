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
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
/**
 * Sample Eureka client that discovers the catalog service using Eureka and sends requests.
 */
public class ExampleEurekaClient {

    private static ApplicationInfoManager applicationInfoManager;
    private static EurekaClient eurekaClient;
    public static String vipAddress = "catalog";

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
     * Gets the list of products by calling the API
     * @return string representation of all the products currently in the product
     */
    private static String getCatalogs() {
        // Create closeable http client to execute requests with
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Creating the request to execute
            String url = getServiceURL() + "/product/catalogGetJson";
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
     * Method for creating a product using the REST API
     * @param productCode of the product
     * @param productName of the product
     * @param description of the product
     * @param price of the product
     */
    private static void createCatalog(String productCode, String productName, String description, double price) {
        String url = getServiceURL();
        String path=url+"/product/%s/%s/%s/%f";
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(String.format(path,
                    productCode,productName,description,price));
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
    @Bean
    static RestTemplate restTemplate() {
        return new RestTemplate();
    }
    public static void main(String[] args) {
        // create the client
        ApplicationInfoManager applicationInfoManager = initializeApplicationInfoManager(new MyDataCenterInstanceConfig());
        EurekaClient client = initializeEurekaClient(applicationInfoManager, new DefaultEurekaClientConfig());
        createCatalog("Book-1001", "WebService","Book",49.99);
        createCatalog("TV-3001", "TV","Electronic_device",599.99);
        createCatalog("Sofa-2001", "Sofa","Furniture",899.99);
        System.out.println("\n"+"Get CATALOG:"+"\n"+getCatalogs());
        // shutdown the client
        eurekaClient.shutdown();
    }

}