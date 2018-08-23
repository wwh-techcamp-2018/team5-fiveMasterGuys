package com.woowahan.techcamp.recipehub.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AcceptanceTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    protected MockMvc mvc;
    protected final String DEFAULT_USER_EMAIL = "WeAreTheBestTeam@recipehub.com";
    protected final String DEFAULT_USER_PASSWORD = "password1234!";
    protected User basicAuthUser;
    protected User savedUser;

    @Before
    public void setUp() throws Exception {
        User.UserBuilder userBuilder = User.builder()
                .email(DEFAULT_USER_EMAIL)
                .password(new BCryptPasswordEncoder().encode(DEFAULT_USER_PASSWORD))
                .name("Team5");
        savedUser = userRepository.save(userBuilder.build());

        basicAuthUser = userBuilder.password(DEFAULT_USER_PASSWORD).build();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    public TestRestTemplate template(User user) {
        return (user == null) ? template : template.withBasicAuth(user.getEmail(), user.getPassword());
    }

    public TestRestTemplate template() {
        return template(null);
    }

    private HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    protected <T, R> ResponseEntity<RestResponse<R>> requestJson(String path, HttpMethod method, T dto, User user, ParameterizedTypeReference<RestResponse<R>> typeRef) {
        return template(user).exchange(path, method, new HttpEntity<>(dto, getJsonHeaders()), typeRef);
    }

    protected <T, R> ResponseEntity<RestResponse<R>> requestJson(String path, HttpMethod method, T dto, ParameterizedTypeReference<RestResponse<R>> typeRef) {
        return requestJson(path, method, dto, null, typeRef);
    }

    protected <R> ResponseEntity<RestResponse<R>> requestJson(String path, HttpMethod method, ParameterizedTypeReference<RestResponse<R>> typeRef) {
        return requestJson(path, method, null, null, typeRef);
    }

    protected <T, R> ResponseEntity<RestResponse<List<R>>> requestJsonList(String path, HttpMethod method, T dto, User user, ParameterizedTypeReference<RestResponse<List<R>>> typeref) {
        return template(user).exchange(path, method, new HttpEntity<>(dto, getJsonHeaders()), typeref);
    }

    protected <T, R> ResponseEntity<RestResponse<List<R>>> requestJsonList(String path, HttpMethod method, T dto, ParameterizedTypeReference<RestResponse<List<R>>> typeRef) {
        return requestJsonList(path, method, dto, null, typeRef);
    }

    protected <R> ResponseEntity<RestResponse<List<R>>> requestJsonList(String path, HttpMethod method, ParameterizedTypeReference<RestResponse<List<R>>> typeRef) {
        return requestJsonList(path, method, null, null, typeRef);
    }

    protected ResponseEntity<String> requestGet(String path, User user) {
        return template(user).getForEntity(path, String.class);
    }

    protected ResponseEntity<String> requestGet(String path) {
        return template(null).getForEntity(path, String.class);
    }

    protected <T> ResponseEntity<String> requestPost(String path, T dto, User user) {
        return template(user).postForEntity(path, request(dto, null), String.class);
    }
    protected <T> ResponseEntity<String> requestPost(String path, T dto) {
        return requestPost(path, dto, null);
    }

    protected <T> ResponseEntity<String> requestPut(String path, T dto, User user) {
        return template(user).postForEntity(path, request(dto, "put"), String.class);
    }

    protected <T> ResponseEntity<String> requestPut(String path, T dto) {
        return requestPut(path, dto, null);
    }

    protected ResponseEntity<String> requestDelete(String path, User user) {
        return template(user).postForEntity(path, request(null, "delete"), String.class);
    }

    protected ResponseEntity<String> requestDelete(String path) {
        return requestDelete(path, null);
    }


    protected ResponseEntity<RestResponse<String>> requestFileUpload(User user, String key, ClassPathResource resource) {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder
                .multipartFormData()
                .addParameter(key, resource)
                .build();
        return template(user).exchange("/images", HttpMethod.POST, request, new ParameterizedTypeReference<RestResponse<String>>() {
        });
    }


    private <T> HttpEntity<MultiValueMap<String, Object>> request(T dto, String method) {
        MultiValueMap<String, Object> params = getBodyParams(dto);
        if (method != null) params.add("_method", method);
        return new HttpEntity<>(params, getHeaders());
    }

    private MultiValueMap<String, Object> getBodyParams(Object dto) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        if (dto == null) return params;
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(dto, Map.class);
        map.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .forEach(e -> params.add(e.getKey(), String.valueOf(e.getValue())));
        return params;
    }

    protected static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
