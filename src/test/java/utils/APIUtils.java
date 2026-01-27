package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIUtils {

    public static Response post(String endpoint, Object body, String token) {
        RequestSpecification request = RestAssured.given()
                .baseUri(ConfigReader.getProperty("api.url"))
                .contentType(ContentType.JSON);

        if (token != null && !token.isEmpty()) {
            request.header("Authorization", "Bearer " + token);
        }

        if (body != null) {
            request.body(body);
        }

        return request.when().post(endpoint);
    }

    public static Response get(String endpoint, String token) {
        RequestSpecification request = RestAssured.given()
                .baseUri(ConfigReader.getProperty("api.url"))
                .contentType(ContentType.JSON);

        if (token != null && !token.isEmpty()) {
            request.header("Authorization", "Bearer " + token);
        }

        return request.when().get(endpoint);
    }
}
