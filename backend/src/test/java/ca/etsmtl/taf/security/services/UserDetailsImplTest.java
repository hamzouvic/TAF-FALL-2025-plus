package ca.etsmtl.taf.security.services;

import ca.etsmtl.taf.entity.ERole;
import ca.etsmtl.taf.entity.Role;
import ca.etsmtl.taf.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsImpl - Unit")
class UserDetailsImplTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user123");
        user.setFullName("Test User");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("hashedpassword");
        user.setRoles(Set.of(new Role(ERole.ROLE_USER)));
    }

    @Test
    void constructorShouldMapFields() {
        UserDetailsImpl details = new UserDetailsImpl(
            "id1",
            "Full Name",
            "uname",
            "mail@test.com",
            "pwd",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        assertEquals("id1", details.getId());
        assertEquals("Full Name", details.getFullName());
        assertEquals("uname", details.getUsername());
        assertEquals("mail@test.com", details.getEmail());
        assertEquals("pwd", details.getPassword());
        assertTrue(details.isEnabled());
    }

    @Test
    void buildShouldCreateAuthoritiesFromRoles() {
        UserDetailsImpl built = UserDetailsImpl.build(user);

        assertNotNull(built);
        assertEquals("testuser", built.getUsername());
        assertEquals("test@example.com", built.getEmail());
        assertFalse(built.getAuthorities().isEmpty());
    }
}
