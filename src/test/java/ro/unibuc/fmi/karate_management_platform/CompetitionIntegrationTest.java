package ro.unibuc.fmi.karate_management_platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompetitionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void coachRegistersAthleteAndRequestAppearsInList() throws Exception {
        String loginJson = "{" +
                "\"email\": \"coach7@karate.com\"," +
                "\"password\": \"Password123!\"," +
                "\"language\": \"en\"}";

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(loginResponse).get("accessToken").asText();
        assertThat(accessToken).isNotBlank();

        Long competitionId = 6L;
        Long athleteId = 34L;
        Map<Long, Set<String>> individualCategories = Collections.singletonMap(34L, Set.of("KATA_INDIVIDUAL", "KUMITE_INDIVIDUAL"));
        Map<Long, Set<String>> teamCategories = Collections.emptyMap();
        String registrationJson = "{" +
                "\"individualCategories\": {\"34\": [\"KATA_INDIVIDUAL\", \"KUMITE_INDIVIDUAL\"]}," +
                "\"teamCategories\": {}" +
                "}";

        mockMvc.perform(post("/api/v1/competitions/{competitionId}/register", competitionId)
                        .header("Authorization", "Bearer " + accessToken)
                        .param("athleteId", athleteId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson))
                .andExpect(status().isOk());

        MvcResult requestsResult = mockMvc.perform(get("/api/v1/requests/made-by-me?page=0&size=10&requestType=ATHLETE_COMPETITION_REGISTRATION")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String requestsResponse = requestsResult.getResponse().getContentAsString();
        assertThat(requestsResponse).contains("KATA_INDIVIDUAL");
        assertThat(requestsResponse).contains("KUMITE_INDIVIDUAL");
        assertThat(requestsResponse).contains("34"); // athleteId
    }

    @Test
    void organizerCreatesCompetitionRequestAndAdminSeesItAssigned() throws Exception {
        java.util.Random random = new java.util.Random();
        int number = 10 + random.nextInt(9991);
        String competitionName = "competition" + number;
        String competitionDate = "2026-" + String.format("%02d", 1 + random.nextInt(12)) + "-" + String.format("%02d", 1 + random.nextInt(28));

        // 1. Login ca organizer3@karate.com
        String organizerLoginJson = "{" +
                "\"email\": \"organizer3@karate.com\"," +
                "\"password\": \"Password123!\"," +
                "\"language\": \"en\"}";

        MvcResult organizerLoginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(organizerLoginJson))
                .andExpect(status().isOk())
                .andReturn();

        String organizerLoginResponse = organizerLoginResult.getResponse().getContentAsString();
        String organizerAccessToken = objectMapper.readTree(organizerLoginResponse).get("accessToken").asText();
        assertThat(organizerAccessToken).isNotBlank();

        // 2. Creează un request de competition creation
        String competitionRequestJson = String.format("{\"name\": \"%s\",\"location\": \"string\",\"date\": \"%s\"}", competitionName, competitionDate);

        mockMvc.perform(post("/api/v1/requests/competitions")
                        .header("Authorization", "Bearer " + organizerAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(competitionRequestJson))
                .andExpect(status().isOk());

        // 3. Login ca admin@admin.com
        String adminLoginJson = "{" +
                "\"email\": \"admin@admin.com\"," +
                "\"password\": \"Password123!\"," +
                "\"language\": \"en\"}";

        MvcResult adminLoginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminLoginJson))
                .andExpect(status().isOk())
                .andReturn();

        String adminLoginResponse = adminLoginResult.getResponse().getContentAsString();
        String adminAccessToken = objectMapper.readTree(adminLoginResponse).get("accessToken").asText();
        assertThat(adminAccessToken).isNotBlank();

        // 4. Verifică assigned-to-me pentru COMPETITION_CREATION
        MvcResult assignedResult = mockMvc.perform(get("/api/v1/requests/assigned-to-me?page=0&size=10&requestType=COMPETITION_CREATION")
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String assignedResponse = assignedResult.getResponse().getContentAsString();
        assertThat(assignedResponse).contains(competitionName); // numele competiției
        assertThat(assignedResponse).contains(competitionDate);
    }
} 