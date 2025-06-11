package com.pharmainventory.inventory.integration;

import com.pharmainventory.inventory.dto.LoginRequest;
import com.pharmainventory.inventory.dto.LoginResponse;
import com.pharmainventory.inventory.dto.MedicineDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void fullFlow_medicineCrud_andSwagger() {
        // Login
        LoginRequest login = new LoginRequest();
        login.setUsername("user");
        login.setPassword("password");
        ResponseEntity<LoginResponse> auth = restTemplate.postForEntity("/auth/login", login, LoginResponse.class);
        assertThat(auth.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = auth.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        // Create Medicine
        MedicineDTO payload = new MedicineDTO(null, "IntTestMed", "TestInc", null, "NORMAL");
        HttpEntity<MedicineDTO> createReq = new HttpEntity<>(payload, headers);
        ResponseEntity<MedicineDTO> create = restTemplate.exchange("/api/medicines", HttpMethod.POST, createReq, MedicineDTO.class);
        assertThat(create.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long id = create.getBody().getId();

        // Get Medicine
        HttpEntity<Void> getReq = new HttpEntity<>(headers);
        ResponseEntity<MedicineDTO> getResp = restTemplate.exchange("/api/medicines/" + id, HttpMethod.GET, getReq, MedicineDTO.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResp.getBody().getName()).isEqualTo("IntTestMed");

        // Swagger UI available
        ResponseEntity<String> docs = restTemplate.getForEntity("/swagger-ui/index.html", String.class);
        assertThat(docs.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
