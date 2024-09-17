package karole;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class apiConnect {

    static String URL = "https://open.er-api.com/v6/latest/USD"; 
    static HttpClient client = HttpClient.newHttpClient();
    static ObjectMapper objectMapper = new ObjectMapper();
    static JsonNode jsonNode;


    static String apiGet() {
        
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            
            return response.body();
        }
        catch (Exception e) {
            System.out.println("ERROR: API connect");
            return "error";
        }
    }


    public static JsonNode generateJsonNode() {
        String apiResponse = apiGet();

        if (!apiResponse.equals("error")) {
            try {
                jsonNode = objectMapper.readTree(apiResponse);
                }
            catch (IOException e) {System.out.println("ERROR: Read API string");}
        }
        return jsonNode;
    }
}
