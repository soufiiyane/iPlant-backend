package com.example.iPlant.Model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MedicinalPlantTest {

    @Test
    public void testGettersAndSetters() {
        MedicinalPlant plant = new MedicinalPlant();
        plant.setName("Aloe Vera");
        plant.setDescription("A medicinal plant");
        plant.setImageUrl("http://example.com/image.jpg");
        plant.setPlantId("plant123");
        List<String> properties = Arrays.asList("Healing", "Soothing");
        plant.setProperties(properties);

        assertEquals("Aloe Vera", plant.getName());
        assertEquals("A medicinal plant", plant.getDescription());
        assertEquals("http://example.com/image.jpg", plant.getImageUrl());
        assertEquals("plant123", plant.getPlantId());
        assertEquals(properties, plant.getProperties());
    }
}