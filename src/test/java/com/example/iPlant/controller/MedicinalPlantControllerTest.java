package com.example.iPlant.controller;

import com.example.iPlant.MedicinalPlantController;
import com.example.iPlant.Model.MedicinalPlant;
import com.example.iPlant.Service.MedicinalPlantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicinalPlantController.class)
public class MedicinalPlantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicinalPlantService plantService;

    @Autowired
    private ObjectMapper objectMapper;

    private MedicinalPlant samplePlant1;
    private MedicinalPlant samplePlant2;
    private List<MedicinalPlant> plantList;

    @BeforeEach
    void setUp() {
        // Set up first sample plant
        samplePlant1 = new MedicinalPlant();
        samplePlant1.setName("Aloe Vera");
        samplePlant1.setDescription("Succulent plant species");
        samplePlant1.setPlantId("1");
        samplePlant1.setProperties(Arrays.asList("Healing", "Anti-inflammatory"));
        samplePlant1.setTags(Arrays.asList("Succulent", "Medicinal"));

        // Set up second sample plant
        samplePlant2 = new MedicinalPlant();
        samplePlant2.setName("Tulsi");
        samplePlant2.setDescription("Holy Basil");
        samplePlant2.setPlantId("2");
        samplePlant2.setProperties(Arrays.asList("Antibacterial", "Adaptogenic"));
        samplePlant2.setTags(Arrays.asList("Herb", "Sacred"));

        plantList = Arrays.asList(samplePlant1, samplePlant2);
    }

    @Test
    public void whenGetAllPlants_thenReturnAllPlants() throws Exception {
        when(plantService.getAllPlants()).thenReturn(plantList);

        mockMvc.perform(get("/api/plants"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Aloe Vera"))
                .andExpect(jsonPath("$[1].name").value("Tulsi"))
                .andExpect(jsonPath("$[0].properties[0]").value("Healing"))
                .andExpect(jsonPath("$[1].properties[0]").value("Antibacterial"));
    }

    @Test
    public void whenGetAllPlants_thenHandleEmptyList() throws Exception {
        when(plantService.getAllPlants()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/plants"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void whenGetAllPlants_thenHandleError() throws Exception {
        when(plantService.getAllPlants()).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/api/plants"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error fetching plants: Service error"));
    }

    @Test
    public void whenSearchByName_thenReturnMatchingPlants() throws Exception {
        when(plantService.getAllPlants()).thenReturn(plantList);

        mockMvc.perform(get("/api/plants/search")
                        .param("query", "aloe")
                        .param("searchType", "name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Aloe Vera"))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void whenSearchByTags_thenReturnMatchingPlants() throws Exception {
        when(plantService.getAllPlants()).thenReturn(plantList);

        mockMvc.perform(get("/api/plants/search")
                        .param("query", "herb")
                        .param("searchType", "tags"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tulsi"))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void whenSearchByProperties_thenReturnMatchingPlants() throws Exception {
        when(plantService.getAllPlants()).thenReturn(plantList);

        mockMvc.perform(get("/api/plants/search")
                        .param("query", "healing")
                        .param("searchType", "properties"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Aloe Vera"))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void whenSearchWithInvalidType_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/plants/search")
                        .param("query", "aloe")
                        .param("searchType", "invalid"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid search type"));
    }

    @Test
    public void whenSearchThrowsException_thenReturnInternalServerError() throws Exception {
        when(plantService.getAllPlants()).thenThrow(new RuntimeException("Search error"));

        mockMvc.perform(get("/api/plants/search")
                        .param("query", "aloe")
                        .param("searchType", "name"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error searching plants: Search error"));
    }

    @Test
    public void whenSearchWithNullParameters_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/plants/search"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenSearchCaseInsensitive_thenReturnMatchingPlants() throws Exception {
        when(plantService.getAllPlants()).thenReturn(plantList);

        mockMvc.perform(get("/api/plants/search")
                        .param("query", "ALOE")
                        .param("searchType", "name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Aloe Vera"));
    }

    @Test
    public void whenSearchByTagsPartialMatch_thenReturnMatchingPlants() throws Exception {
        when(plantService.getAllPlants()).thenReturn(plantList);

        mockMvc.perform(get("/api/plants/search")
                        .param("query", "med")
                        .param("searchType", "tags"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Aloe Vera"))
                .andExpect(jsonPath("$[0].tags[1]").value("Medicinal"));
    }

    @Test
    public void whenSearchByPropertiesMultipleMatches_thenReturnAllMatches() throws Exception {
        // Add another plant with similar properties
        MedicinalPlant samplePlant3 = new MedicinalPlant();
        samplePlant3.setName("Chamomile");
        samplePlant3.setProperties(Arrays.asList("Anti-inflammatory", "Calming"));
        List<MedicinalPlant> extendedList = new ArrayList<>(plantList);
        extendedList.add(samplePlant3);

        when(plantService.getAllPlants()).thenReturn(extendedList);

        mockMvc.perform(get("/api/plants/search")
                        .param("query", "inflammatory")
                        .param("searchType", "properties"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Aloe Vera"))
                .andExpect(jsonPath("$[1].name").value("Chamomile"));
    }

    @Test
    public void whenSearchWithSpecialCharacters_thenHandleGracefully() throws Exception {
        when(plantService.getAllPlants()).thenReturn(plantList);

        mockMvc.perform(get("/api/plants/search")
                        .param("query", "aloe&vera*")
                        .param("searchType", "name"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
