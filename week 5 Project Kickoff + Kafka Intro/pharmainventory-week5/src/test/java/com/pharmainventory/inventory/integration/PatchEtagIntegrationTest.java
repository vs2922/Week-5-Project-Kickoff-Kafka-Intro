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
public class PatchEtagIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void patchWithEtag_andConditionalGet() {
        // Login
        LoginRequest login = new LoginRequest();
        login.setUsername("user");
        login.setPassword("password");
        String token = restTemplate.postForEntity("/auth/login", login, LoginResponse.class).getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        // Create Medicine
        MedicineDTO payload = new MedicineDTO(null, "PatchMed", "Pharma", null, "NORMAL");
        HttpEntity<MedicineDTO> createReq = new HttpEntity<>(payload, headers);
        MedicineDTO created = restTemplate.exchange("/api/medicines", HttpMethod.POST, createReq, MedicineDTO.class).getBody();
        Long id = created.getId();

        // GET to retrieve ETag
        HttpEntity<Void> getReq = new HttpEntity<>(headers);
        ResponseEntity<MedicineDTO> resp1 = restTemplate.exchange("/api/medicines/" + id, HttpMethod.GET, getReq, MedicineDTO.class);
        String etag = resp1.getHeaders().getETag();

        // Conditional GET with If-None-Match
        HttpHeaders condHeaders = new HttpHeaders();
        condHeaders.set("If-None-Match", etag);
        HttpEntity<Void> condReq = new HttpEntity<>(condHeaders);
        ResponseEntity<Void> condResp = restTemplate.exchange("/api/medicines/" + id, HttpMethod.GET, condReq, Void.class);
        assertThat(condResp.getStatusCode()).isEqualTo(HttpStatus.NOT_MODIFIED);

        // Patch operation
        String patchJson = "[{ \"op\": \"replace\", \"path\": \"/name\", \"value\": \"PatchedMed\" }]";
        HttpHeaders patchHeaders = new HttpHeaders();
        patchHeaders.setContentType(MediaType.valueOf("application/json-patch+json"));
        patchHeaders.setBearerAuth(token);
        HttpEntity<String> patchReq = new HttpEntity<>(patchJson, patchHeaders);
        ResponseEntity<MedicineDTO> patchResp = restTemplate.exchange("/api/medicines/" + id, HttpMethod.PATCH, patchReq, MedicineDTO.class);
        assertThat(patchResp.getBody().getName()).isEqualTo("PatchedMed");
    }
}
