package com.example.iPlant.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {
    @JsonProperty("PlantId")
    private String plantId;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("Text")
    private String text;

    @JsonProperty("UserId")
    private String userId;

    @JsonProperty("UserImageUrl")
    private String userImageUrl;

    // Getters and Setters
    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "plantId='" + plantId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", text='" + text + '\'' +
                ", userId='" + userId + '\'' +
                ", userImageUrl='" + userImageUrl + '\'' +
                '}';
    }
}