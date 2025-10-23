package com.donation.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Donor data transfer object
 * Used for data transfer between frontend and backend
 */
public class DonorDTO {
    
    private Long id;
    
    @NotBlank(message = "Donor name cannot be empty")
    @Size(max = 100, message = "Name length cannot exceed 100 characters")
    private String name;
    
    @Size(max = 200, message = "Contact information length cannot exceed 200 characters")
    private String contactInfo;
    
    private LocalDateTime createdAt;
    
    private List<DonationDTO> donations;
    
    // constructor
    public DonorDTO() {}
    
    public DonorDTO(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }
    
    // Getter and setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<DonationDTO> getDonations() {
        return donations;
    }
    
    public void setDonations(List<DonationDTO> donations) {
        this.donations = donations;
    }
    
    @Override
    public String toString() {
        return "DonorDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
