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
 * Sample Eureka client that discovers the order service using Eureka and sends requests.
 */
public class ExampleEurekaClient {

    private static ApplicationInfoManager applicationInfoManager;
    private static EurekaClient eurekaClient;
    public static String vipAddress = "order";

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
     * Gets the list of orders by calling the API in Json format
     * @return string representation of all the orders currently in the order
     */
    private static String getOrders() {
        // Create closeable http client to execute requests with
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Creating the request to execute
            String url = getServiceURL() + "/orderGetJson";
            HttpGet httpget = new HttpGet(url);
            // Executing the request using the http client and obtaining the response
            CloseableHttpResponse response = client.execute(httpget);
            String responseJsonString=restTemplate().getForEntity(url ,String.class).getBody();
            return readResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to get order";
        }
    }

    /**
     * Method for creating a order using the REST API
     * @param customerEmail of the order
     * @param customerAddress of the order
     * @param productName of the order
     * @param productCode of the order
     */
    private static void createOrder(String customerEmail, String customerAddress, String productName, String productCode) {
        String url = getServiceURL();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(String.format("http://localhost:8082/order/%s/%s/%s/%s",
                    customerEmail, customerAddress,productName,productCode));
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
       System.out.println("Response AAA:"+response.toString());
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
    @Bean
    static RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        ExampleEurekaClient sampleClient = new ExampleEurekaClient();
        // create the client
        ApplicationInfoManager applicationInfoManager = initializeApplicationInfoManager(new MyDataCenterInstanceConfig());
        EurekaClient client = initializeEurekaClient(applicationInfoManager, new DefaultEurekaClientConfig());

        createOrder("Ali***@gmail.com","360***Ave.","Book","Book-1001");
        createOrder("Nick***@gmail.com","4503***Ave.","Sofa","Sofa-2001");
        createOrder("Hamed***@gmail.com","234***Ave.","TV","TV-3001");
        createOrder("Kosta***@gmail.com","567***Ave.","TV","TV-3001");
        System.out.println("\n"+"Get Order:"+"\n"+ getOrders());
        // shutdown the client
        eurekaClient.shutdown();
    }

}