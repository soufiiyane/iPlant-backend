package com.example.iPlant.Service;

import com.example.iPlant.Model.MedicinalPlant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MedicinalPlantServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MedicinalPlantService medicinalPlantService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPlants() throws Exception {
        String jsonResponse = "{\"body\":\"{\\\"items\\\":[{\\\"Name\\\":\\\"Aloe Vera\\\",\\\"Description\\\":\\\"A medicinal plant\\\",\\\"ImageS3Url\\\":\\\"http://example.com/image.jpg\\\",\\\"PlantId\\\":\\\"plant123\\\"}]}\"}";

        when(restTemplate.getForEntity(any(String.class), any(Class.class)))
                .thenReturn(new ResponseEntity<>(jsonResponse, HttpStatus.OK));

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("body", "{\"items\":[{\"Name\":\"Aloe Vera\",\"Description\":\"A medicinal plant\",\"ImageS3Url\":\"http://example.com/image.jpg\",\"PlantId\":\"plant123\"}]}");

        Map<String, Object> bodyMap = new HashMap<>();
        Map<String, String> plantDetails = new HashMap<>();
        plantDetails.put("Name", "Aloe Vera");
        plantDetails.put("Description", "A medicinal plant");
        plantDetails.put("ImageS3Url", "http://example.com/image.jpg");
        plantDetails.put("PlantId", "plant123");

        bodyMap.put("items", Collections.singletonList(plantDetails));

        when(objectMapper.readValue(any(String.class), any(Class.class)))
                .thenReturn(responseBody)
                .thenReturn(bodyMap);

        List<MedicinalPlant> plants = medicinalPlantService.getAllPlants();

        assertEquals(1, plants.size());
        assertEquals("Aloe Vera", plants.get(0).getName());
        assertEquals("A medicinal plant", plants.get(0).getDescription());
        assertEquals("http://example.com/image.jpg", plants.get(0).getImageUrl());
        assertEquals("plant123", plants.get(0).getPlantId());
    }
}