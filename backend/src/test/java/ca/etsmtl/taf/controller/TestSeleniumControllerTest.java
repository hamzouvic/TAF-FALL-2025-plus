package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.entity.SeleniumCaseResponse;
import ca.etsmtl.taf.service.SeleniumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("TestSeleniumController - Unit")
class TestSeleniumControllerTest {

    @Mock
    private SeleniumService seleniumService;

    @InjectMocks
    private TestSeleniumController controller;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("runTests should return service response")
    void runTestsShouldReturnOk() throws Exception {
        SeleniumCaseResponse response = new SeleniumCaseResponse();
        response.setOutput("ok");
        response.setSuccess(true);
        response.setDuration(10L);
        response.setTimestamp(System.currentTimeMillis());

        when(seleniumService.sendTestCases(any())).thenReturn(List.of(response));

        String body = "[{\"case_id\":1,\"caseName\":\"Login\",\"actions\":[]}]";

        mockMvc.perform(post("/api/testselenium")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].output").value("ok"))
            .andExpect(jsonPath("$[0].success").value(true));
    }
}
