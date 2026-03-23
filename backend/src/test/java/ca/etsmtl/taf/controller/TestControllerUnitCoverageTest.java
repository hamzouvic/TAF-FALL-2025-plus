package ca.etsmtl.taf.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TestController helper methods coverage")
class TestControllerUnitCoverageTest {

    private final TestController controller = new TestController();

    @Test
    @DisplayName("normalizeMessage handles null, blank and multi-spaces")
    void normalizeMessageCoversAllBranches() {
        assertEquals("", controller.normalizeMessage(null));
        assertEquals("", controller.normalizeMessage("   \t  "));
        assertEquals("hello world", controller.normalizeMessage("  hello   world  "));
    }

    @Test
    @DisplayName("hasAdminAccess accepts ADMIN and ROLE_ADMIN")
    void hasAdminAccessPositiveCases() {
        assertTrue(controller.hasAdminAccess("ADMIN"));
        assertTrue(controller.hasAdminAccess(" role_admin "));
    }

    @Test
    @DisplayName("hasAdminAccess rejects null and non-admin roles")
    void hasAdminAccessNegativeCases() {
        assertFalse(controller.hasAdminAccess(null));
        assertFalse(controller.hasAdminAccess("USER"));
    }
}
