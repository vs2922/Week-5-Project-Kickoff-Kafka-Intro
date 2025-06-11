package com.pharmainventory.inventory.integration;

import com.pharmainventory.inventory.dto.LoginRequest;
import com.pharmainventory.inventory.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaginationSecurityIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void unauthorizedAccess_isRejected() {
        ResponseEntity<String> resp = restTemplate.getForEntity("/api/medicines?page=0&size=5", String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void paginationAndLinks_workWithJwt() {
        // authenticate
        LoginRequest login = new LoginRequest();
        login.setUsername("user");
        login.setPassword("password");
        String token = restTemplate.postForEntity("/auth/login", login, LoginResponse.class).getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        // request first page
        HttpEntity<Void> req = new HttpEntity<>(headers);
        ResponseEntity<String> pageResp = restTemplate.exchange(
            "/api/medicines?page=0&size=1",
            HttpMethod.GET, req, String.class
        );

        assertThat(pageResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        String body = pageResp.getBody();
        assertThat(body).contains("\"_links\"");
        assertThat(body).contains("\"page\":{\"size\":1");
    }
}
