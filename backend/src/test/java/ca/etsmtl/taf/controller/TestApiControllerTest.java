package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.payload.request.TestApiRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("TestApiController - Unit")
class TestApiControllerTest {

    @Test
    @DisplayName("testApi should throw URISyntaxException when URL is invalid")
    void testApiShouldThrowForInvalidUri() {
        TestApiController controller = new TestApiController();
        controller.Test_API_microservice_url = "http://bad host";
        controller.Test_API_microservice_port = "8080";

        TestApiRequest request = new TestApiRequest();
        request.setMethod("GET");
        request.setApiUrl("https://example.com");
        request.setStatusCode(200);
        request.setInput("{}");
        request.setExpectedOutput("ok");

        assertThrows(URISyntaxException.class, () -> controller.testApi(request));
    }
}
