package com.khomishchak.ws.controllers;

import com.khomishchak.ws.model.enums.DeviceType;
import com.khomishchak.ws.model.enums.UserRole;
import com.khomishchak.ws.model.requests.LoginRequest;
import com.khomishchak.ws.model.requests.RegistrationRequest;
import com.khomishchak.ws.model.response.LoginResult;
import com.khomishchak.ws.model.response.RegistrationResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldRegisterAccount() {
        // given
        RegistrationRequest registrationRequest = new RegistrationRequest("testU", "testP",
                "testE@gmail.com", true, DeviceType.WEB);

        // when
        ResponseEntity<RegistrationResult> result = restTemplate.exchange("/api/v1/auth/register", HttpMethod.POST,
                new HttpEntity<RegistrationRequest>(registrationRequest), RegistrationResult.class);

        // then
        RegistrationResult actualResult = result.getBody();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertAll("Registration result validation",
                () -> assertThat(actualResult).isNotNull(),
                () -> assertThat(actualResult.jwt()).isNotBlank(),
                () -> assertThat(actualResult.email()).isEqualTo("testE@gmail.com"),
                () -> assertThat(actualResult.username()).isEqualTo("testU"),
                () -> assertThat(actualResult.userRole()).isEqualTo(UserRole.USER)
        );
    }

    @Test
    void shouldLoginAccount_whenAccountDoExists() {
        // given
        LoginRequest loginRequest = new LoginRequest("testU", "testP", DeviceType.WEB);

        // when
        ResponseEntity<LoginResult> result = restTemplate.exchange("/api/v1/auth/login", HttpMethod.POST,
                new HttpEntity<LoginRequest>(loginRequest), LoginResult.class);

        // then
        LoginResult actualResult = result.getBody();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertAll("Login result validation",
                () -> assertThat(actualResult).isNotNull(),
                () -> assertThat(actualResult.jwt()).isNotBlank(),
                () -> assertThat(actualResult.username()).isEqualTo("testU")
        );
    }

    @Test
    void shouldLoginAccount_whenAccountDoNotExists() {
        // given
        LoginRequest loginRequest = new LoginRequest("wrongU", "wrongP", DeviceType.WEB);

        // when
        ResponseEntity<LoginResult> result = restTemplate.exchange("/api/v1/auth/login", HttpMethod.POST,
                new HttpEntity<LoginRequest>(loginRequest), LoginResult.class);

        // then
        LoginResult actualResult = result.getBody();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(actualResult).isNull();
    }
}