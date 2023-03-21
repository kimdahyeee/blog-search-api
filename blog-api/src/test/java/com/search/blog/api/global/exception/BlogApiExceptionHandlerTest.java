package com.search.blog.api.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.search.blog.api.core.error.response.ErrorCode;
import com.search.blog.api.test.config.TestProfile;
import com.search.blog.core.exception.BusinessAlarmException;
import com.search.blog.core.exception.BusinessException;
import com.search.blog.core.exception.NotSupportException;
import com.search.blog.core.logger.data.KvData;
import lombok.Getter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles(TestProfile.TEST)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("controller advice response check")
class BlogApiExceptionHandlerTest {

    private static final String BUSINESS_EXCEPTION_URI = "/businessException";
    private static final String BUSINESS_EXCEPTION_CUSTOM_URI = "/businessException/custom";
    private static final String BUSINESS_ALARM_EXCEPTION_URI = "/businessAlarmException";
    private static final String NOT_SUPPORT_EXCEPTION_URI = "/notSupportException";
    private static final String BIND_EXCEPTION_URI = "/methodArgumentNotValidException";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = buildObjectMapper();

    private ObjectMapper buildObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @BeforeAll
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(ErrorController.class)
                .setControllerAdvice(new BlogApiExceptionHandler())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("BusinessException 반환 시 응답")
    void businessException() throws Exception {
        mockMvc.perform(get(BUSINESS_EXCEPTION_URI))
                .andExpect(jsonPath("$.status", is(ErrorCode.BAD_REQUEST.getStatus())))
                .andExpect(jsonPath("$.errors", is(empty())));
    }

    @Test
    @DisplayName("custom BusinessException 반환 시 응답")
    void customBusinessException() throws Exception {
        mockMvc.perform(get(BUSINESS_EXCEPTION_CUSTOM_URI))
                .andExpect(jsonPath("$.status", is(ErrorCode.BAD_REQUEST.getStatus())))
                .andExpect(jsonPath("$.message", is(CustomBusinessException.MESSAGE)))
                .andExpect(jsonPath("$.errors", is(empty())));
    }

    @ParameterizedTest
    @CsvSource({"a,1"})
    @DisplayName("BusinessAlarmException 반환 시 logging 처리와 개발자 알림 처리를 수행한다.")
    void businessAlarmException(String stringParam, Long longParam) throws Exception {
        mockMvc.perform(
                get(BUSINESS_ALARM_EXCEPTION_URI)
                        .param("stringParam", stringParam)
                        .param("longParam", String.valueOf(longParam))
        )
                .andExpect(jsonPath("$.status", is(ErrorCode.BAD_REQUEST.getStatus())));
    }

    @Test
    @DisplayName("MethodArgumentTypeMismatchException 을 반환 시 400 code 와 함께 사유를 반환한다.")
    void methodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get(NOT_SUPPORT_EXCEPTION_URI).param("testEnum", "test"))
                .andExpect(jsonPath("$.status", is(ErrorCode.BAD_REQUEST.getStatus())))
                .andExpect(jsonPath("$.errors[0].reason", is("typeMismatch")));
    }

    @Test
    @DisplayName("MissingServletRequestParameterException 반환 시 400 code 와 함께 사유를 반환한다.")
    void missingServletRequestParameterException() throws Exception {
        mockMvc.perform(get(NOT_SUPPORT_EXCEPTION_URI))
                .andExpect(jsonPath("$.status", is(ErrorCode.BAD_REQUEST.getStatus())))
                .andExpect(jsonPath("$.errors[0].reason", is("is not present")));
    }

    @Test
    @DisplayName("MethodArgumentNotValidException 반환 시 400 code 와 함께 사유를 validation 오류 난 갯수만큼 반환한다.")
    void methodArgumentNotValidException() throws Exception {
        String content = objectMapper.writeValueAsString(new ErrorController.TestRequest());
        mockMvc.perform(post(BIND_EXCEPTION_URI)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(ErrorCode.BAD_REQUEST.getStatus())))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @RestController
    static class ErrorController {

        @GetMapping(BUSINESS_EXCEPTION_URI)
        void throwsBusinessException() {
            throw new BusinessException();
        }

        @GetMapping(BUSINESS_EXCEPTION_CUSTOM_URI)
        void throwsCustomBusinessException() {
            throw new CustomBusinessException();
        }

        @GetMapping(BUSINESS_ALARM_EXCEPTION_URI)
        void throwsBusinessAlarmException(@RequestParam("stringParam") String stringParam, @RequestParam("longParam") Long longParam) {
            throw new BusinessAlarmException(
                    "business alarm exception",
                    KvData.of("stringParam", stringParam),
                    KvData.of("longParam", longParam));
        }

        @GetMapping(NOT_SUPPORT_EXCEPTION_URI)
        void throwsNotSupportException(@RequestParam("testEnum") TestEnum testEnum) {
            throw new NotSupportException();
        }

        @PostMapping(value = BIND_EXCEPTION_URI)
        void throwsWebExchangeBindException(@RequestBody @Valid TestRequest request) {
        }

        @Getter
        static class TestRequest {

            @NotEmpty(message = "아이디를 입력해주세요.")
            private String id;

            @NotEmpty(message = "이름을 입력해주세요.")
            private String name;

        }

        @Getter
        enum TestEnum {
            FIRST, SECOND
        }

    }

    static class CustomBusinessException extends BusinessException {

        static final String MESSAGE = "커스텀 에러입니다.";

        public CustomBusinessException() {
            super(MESSAGE);
        }

    }

}