package com.example.iPlant;

import com.example.iPlant.Model.MedicinalPlant;
import com.example.iPlant.Service.MedicinalPlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plants")
public class MedicinalPlantController {
    private final MedicinalPlantService plantService;

    @Autowired
    public MedicinalPlantController(MedicinalPlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPlants() {
        try {
            List<MedicinalPlant> plants = plantService.getAllPlants();
            return ResponseEntity.ok(plants);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body("Error fetching plants: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPlants(
            @RequestParam String query,
            @RequestParam String searchType) {
        try {
            List<MedicinalPlant> allPlants = plantService.getAllPlants();
            List<MedicinalPlant> filteredPlants;

            switch (searchType.toLowerCase()) {
                case "name":
                    filteredPlants = allPlants.stream()
                            .filter(plant -> plant.getName().toLowerCase().contains(query.toLowerCase()))
                            .collect(Collectors.toList());
                    break;
                case "tags":
                    filteredPlants = allPlants.stream()
                            .filter(plant -> plant.getTags().stream()
                                    .anyMatch(tag -> tag.toLowerCase().contains(query.toLowerCase())))
                            .collect(Collectors.toList());
                    break;
                case "properties":
                    filteredPlants = allPlants.stream()
                            .filter(plant -> plant.getProperties().stream()
                                    .anyMatch(prop -> prop.toLowerCase().contains(query.toLowerCase())))
                            .collect(Collectors.toList());
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid search type");
            }

            return ResponseEntity.ok(filteredPlants);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body("Error searching plants: " + e.getMessage());
        }
    }
}