/*
https://jsonplaceholder.typicode.com/
https://youtu.be/MAw5Ku1OVFA?si=rJ8SeIWUDVZdu_cN
 */


package learning;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class apiCall {

    // static String URL = "https://jsonplaceholder.typicode.com/posts";
    static String URL = "https://open.er-api.com/v6/latest/USD"; 
    
    static HttpClient client = HttpClient.newHttpClient();
    static ObjectMapper objectMapper = new ObjectMapper();
    static JsonNode jsonNode;
    
    static File file = new File("./docs/learning/apiResponse.json");


    static String apiCallGet() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        return response.body();
    }



    public static void main(String[] args) throws IOException, InterruptedException{
    
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        jsonNode = objectMapper.readTree(apiCallGet());
        objectMapper.writeValue(file, jsonNode);
    }
}
