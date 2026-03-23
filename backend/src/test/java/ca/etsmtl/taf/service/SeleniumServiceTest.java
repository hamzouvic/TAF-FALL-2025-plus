package ca.etsmtl.taf.service;

import ca.etsmtl.taf.apiCommunication.SeleniumServiceRequester;
import ca.etsmtl.taf.dto.SeleniumCaseDto;
import ca.etsmtl.taf.entity.SeleniumCaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SeleniumService - Unit")
class SeleniumServiceTest {

    @Mock
    private SeleniumServiceRequester seleniumServiceRequester;

    @InjectMocks
    private SeleniumService seleniumService;

    private SeleniumCaseDto buildCase(int id, String name) {
        try {
            SeleniumCaseDto dto = new SeleniumCaseDto();
            Field idField = SeleniumCaseDto.class.getDeclaredField("case_id");
            idField.setAccessible(true);
            idField.set(dto, id);

            Field nameField = SeleniumCaseDto.class.getDeclaredField("caseName");
            nameField.setAccessible(true);
            nameField.set(dto, name);
            return dto;
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    @DisplayName("sendTestCases should return empty list for empty input")
    void sendTestCasesEmpty() throws Exception {
        List<SeleniumCaseResponse> result = seleniumService.sendTestCases(new ArrayList<>());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("sendTestCases should map one request to one response")
    void sendTestCasesSingle() throws Exception {
        SeleniumCaseResponse response = new SeleniumCaseResponse();
        response.setOutput("single");
        response.setSuccess(true);

        when(seleniumServiceRequester.sendTestCase(any())).thenReturn(Mono.just(response));

        List<SeleniumCaseResponse> result = seleniumService.sendTestCases(List.of(buildCase(1, "Case 1")));

        assertEquals(1, result.size());
        assertEquals("single", result.get(0).getOutput());
        verify(seleniumServiceRequester, times(1)).sendTestCase(any());
    }

    @Test
    @DisplayName("sendTestCases should process all items")
    void sendTestCasesMultiple() throws Exception {
        SeleniumCaseResponse r1 = new SeleniumCaseResponse();
        r1.setOutput("r1");
        SeleniumCaseResponse r2 = new SeleniumCaseResponse();
        r2.setOutput("r2");

        when(seleniumServiceRequester.sendTestCase(any())).thenReturn(Mono.just(r1), Mono.just(r2));

        List<SeleniumCaseResponse> result = seleniumService.sendTestCases(List.of(buildCase(1, "A"), buildCase(2, "B")));

        assertEquals(2, result.size());
        assertEquals("r1", result.get(0).getOutput());
        assertEquals("r2", result.get(1).getOutput());
        verify(seleniumServiceRequester, times(2)).sendTestCase(any());
    }
}
