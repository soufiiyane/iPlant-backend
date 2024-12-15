package com.example.iPlant.Service;

import com.example.iPlant.Model.MedicinalPlant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MedicinalPlantService {
    private final RestTemplate restTemplate;
    private final String API_URL = "https://ssmb5oqxxa.execute-api.us-east-1.amazonaws.com/dev/medicinalPlants";

    @Autowired
    public MedicinalPlantService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<MedicinalPlant> getAllPlants() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> responseBody = mapper.readValue(response.getBody(), Map.class);
                String body = (String) responseBody.get("body");
                Map<String, Object> bodyMap = mapper.readValue(body, Map.class);
                List<Map<String, Object>> items = (List<Map<String, Object>>) bodyMap.get("items");

                return items.stream()
                        .map(item -> convertToMedicinalPlant(item))
                        .collect(Collectors.toList());
            } else {
                throw new RuntimeException("Failed to fetch plants: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching plants: " + e.getMessage());
        }
    }

    private MedicinalPlant convertToMedicinalPlant(Map<String, Object> item) {
        MedicinalPlant plant = new MedicinalPlant();
        plant.setName((String) item.get("Name"));
        plant.setDescription((String) item.get("Description"));
        plant.setImageUrl((String) item.get("ImageS3Url"));
        plant.setPlantId((String) item.get("PlantId"));

        // Convert complex structures
        plant.setProperties(convertListWithS((List<Map<String, String>>) item.get("Properties")));
        plant.setUses(convertListWithS((List<Map<String, String>>) item.get("Uses")));
        plant.setTags(convertListWithS((List<Map<String, String>>) item.get("Tags")));
        plant.setArticles(convertListWithS((List<Map<String, String>>) item.get("Articles")));
        plant.setPrecautions(convertListWithS((List<Map<String, String>>) item.get("Precautions")));
        plant.setRegion(convertListWithS((List<Map<String, String>>) item.get("Region")));

        // Convert comments
        List<Map<String, String>> comments = convertComments((List<Map<String, Object>>) item.get("Comments"));
        plant.setComments(comments);

        return plant;
    }

    private List<String> convertListWithS(List<Map<String, String>> list) {
        if (list == null) return new ArrayList<>();
        return list.stream()
                .map(map -> map.get("S"))
                .collect(Collectors.toList());
    }

    private List<Map<String, String>> convertComments(List<Map<String, Object>> comments) {
        if (comments == null) return new ArrayList<>();
        return comments.stream()
                .map(comment -> {
                    Map<String, Object> m = (Map<String, Object>) comment.get("M");
                    Map<String, String> convertedComment = new HashMap<>();
                    convertedComment.put("text", getStringValue(m, "Text"));
                    convertedComment.put("userId", getStringValue(m, "UserId"));
                    convertedComment.put("FirstName", getStringValue(m, "FirstName"));
                    convertedComment.put("LastName", getStringValue(m, "LastName"));
                    convertedComment.put("UserImageUrl", getStringValue(m, "UserImageUrl"));
                    return convertedComment;
                })
                .collect(Collectors.toList());
    }

    private String getStringValue(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) return "";
        Map<String, String> value = (Map<String, String>) map.get(key);
        return value != null ? value.get("S") : "";
    }
}
