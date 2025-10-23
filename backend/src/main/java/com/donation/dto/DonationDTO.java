package com.donation.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Donation record data transfer object
 * Used for data transfer between frontend and backend
 */
public class DonationDTO {
    
    private Long id;
    
    @NotNull(message = "Donor ID cannot be empty")
    private Long donorId;
    
    @NotBlank(message = "Donation type cannot be empty")
    @Size(max = 50, message = "Donation type length cannot exceed 50 characters")
    private String donationType;
    
    @NotNull(message = "Donation amount can't be empty")
    @Positive(message = "Donation quantity must be greater than 0")
    private Double quantity;
    
    @Size(max = 20, message = "Unit length cannot exceed 20 characters")
    private String unit = "pieces";
    
    private String description;
    
    private LocalDateTime donationDate;
    
    // associated information
    private String donorName;
    private List<DistributionDTO> distributions;
    
    // constructor
    public DonationDTO() {}
    
    public DonationDTO(Long donorId, String donationType, Double quantity, String unit, String description) {
        this.donorId = donorId;
        this.donationType = donationType;
        this.quantity = quantity;
        this.unit = unit;
        this.description = description;
    }
    
    // Getter and setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getDonorId() {
        return donorId;
    }
    
    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }
    
    public String getDonationType() {
        return donationType;
    }
    
    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }
    
    public Double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getDonationDate() {
        return donationDate;
    }
    
    public void setDonationDate(LocalDateTime donationDate) {
        this.donationDate = donationDate;
    }
    
    public String getDonorName() {
        return donorName;
    }
    
    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }
    
    public List<DistributionDTO> getDistributions() {
        return distributions;
    }
    
    public void setDistributions(List<DistributionDTO> distributions) {
        this.distributions = distributions;
    }
    
    @Override
    public String toString() {
        return "DonationDTO{" +
                "id=" + id +
                ", donorId=" + donorId +
                ", donationType='" + donationType + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", description='" + description + '\'' +
                ", donationDate=" + donationDate +
                ", donorName='" + donorName + '\'' +
                '}';
    }
}
