package ca.etsmtl.taf.payload.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("SignupRequest - Unit")
class SignupRequestTest {

    @Test
    @DisplayName("should set and get all fields")
    void shouldSetAndGetAllFields() {
        SignupRequest request = new SignupRequest();

        request.setFullName("Hamza Afif");
        request.setUsername("hamza");
        request.setEmail("hamza@example.com");
        request.setPassword("secret123");
        request.setRole(Set.of("admin", "user"));

        assertEquals("Hamza Afif", request.getFullName());
        assertEquals("hamza", request.getUsername());
        assertEquals("hamza@example.com", request.getEmail());
        assertEquals("secret123", request.getPassword());
        assertEquals(Set.of("admin", "user"), request.getRole());
    }
}
