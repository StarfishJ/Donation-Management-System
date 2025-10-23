package com.donation.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Distribution record data transfer object
 * Used for data transfer between frontend and backend
 */
public class DistributionDTO {
    
    private Long id;
    
    @NotNull(message = "Donation record ID cannot be empty")
    private Long donationId;
    
    @NotNull(message = "Distribution quantity cannot be empty")
    @Positive(message = "Distribution quantity must be greater than 0")
    private Double quantityDistributed;
    
    @NotNull(message = "Recipient name cannot be empty")
    @Size(max = 100, message = "Recipient name length cannot exceed 100 characters")
    private String recipientName;
    
    @NotNull(message = "Recipient contact cannot be empty")
    @Size(max = 200, message = "Recipient contact length cannot exceed 200 characters")
    private String recipientContact;
    
    @Size(max = 200, message = "Recipient information length cannot exceed 200 characters")
    private String recipientInfo;
    
    private LocalDateTime distributionDate;
    
    private String notes;
    
    // Associated information
    private String donationType;
    private String donorName;
    
    // Constructor
    public DistributionDTO() {}
    
    public DistributionDTO(Long donationId, Double quantityDistributed, String recipientInfo, String notes) {
        this.donationId = donationId;
        this.quantityDistributed = quantityDistributed;
        this.recipientInfo = recipientInfo;
        this.notes = notes;
    }
    
    // Getter and setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getDonationId() {
        return donationId;
    }
    
    public void setDonationId(Long donationId) {
        this.donationId = donationId;
    }
    
    public Double getQuantityDistributed() {
        return quantityDistributed;
    }
    
    public void setQuantityDistributed(Double quantityDistributed) {
        this.quantityDistributed = quantityDistributed;
    }
    
    public String getRecipientName() {
        return recipientName;
    }
    
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
    
    public String getRecipientContact() {
        return recipientContact;
    }
    
    public void setRecipientContact(String recipientContact) {
        this.recipientContact = recipientContact;
    }
    
    public String getRecipientInfo() {
        return recipientInfo;
    }
    
    public void setRecipientInfo(String recipientInfo) {
        this.recipientInfo = recipientInfo;
    }
    
    public LocalDateTime getDistributionDate() {
        return distributionDate;
    }
    
    public void setDistributionDate(LocalDateTime distributionDate) {
        this.distributionDate = distributionDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getDonationType() {
        return donationType;
    }
    
    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }
    
    public String getDonorName() {
        return donorName;
    }
    
    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }
    
    @Override
    //toString method
    public String toString() {
        return "DistributionDTO{" +
                "id=" + id +
                ", donationId=" + donationId +
                ", quantityDistributed=" + quantityDistributed +
                ", recipientInfo='" + recipientInfo + '\'' +
                ", distributionDate=" + distributionDate +
                ", notes='" + notes + '\'' +
                '}';
    }
}
