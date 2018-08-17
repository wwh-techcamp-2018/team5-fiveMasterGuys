package com.woowahan.techcamp.recipehub.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.domain.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

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

    @Before
    public void setUp() throws Exception {
        userRepository.save(User.builder()
                .email(DEFAULT_USER_EMAIL)
                .password(new BCryptPasswordEncoder().encode(DEFAULT_USER_PASSWORD))
                .name("Team5").build());
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

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected <T, R> ResponseEntity<RestResponse<R>> request(String path, HttpMethod method, T dto, User user, ParameterizedTypeReference<RestResponse<R>> typeRef) {
        return template(user).exchange(path, method, new HttpEntity<>(dto, getHeaders()), typeRef);
    }

    protected <T, R> ResponseEntity<RestResponse<R>> request(String path, HttpMethod method, T dto, ParameterizedTypeReference<RestResponse<R>> typeRef) {
        return request(path, method, dto, null, typeRef);
    }

    protected <T, R> ResponseEntity<RestResponse<R>> request(String path, HttpMethod method, ParameterizedTypeReference<RestResponse<R>> typeRef) {
        return request(path, method, null, null, typeRef);
    }

    protected <T, R> ResponseEntity<RestResponse<List<R>>> requestList(String path, HttpMethod method, T dto, User user, ParameterizedTypeReference<RestResponse<List<R>>> typeref) {
        return template(user).exchange(path, method, new HttpEntity<>(dto, getHeaders()), typeref);
    }

    protected <T, R> ResponseEntity<RestResponse<List<R>>> requestList(String path, HttpMethod method, T dto, ParameterizedTypeReference<RestResponse<List<R>>> typeRef) {
        return requestList(path, method, dto, null, typeRef);
    }

    protected <T, R> ResponseEntity<RestResponse<List<R>>> requestList(String path, HttpMethod method, ParameterizedTypeReference<RestResponse<List<R>>> typeRef) {
        return requestList(path, method, null, null, typeRef);
    }

    protected static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
