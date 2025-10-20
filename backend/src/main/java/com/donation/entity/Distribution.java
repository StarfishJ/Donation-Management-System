package com.donation.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Distribution Entity Class
 * Represents distribution information in the system
 */
@Entity
@Table(name = "distributions")
public class Distribution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Donation record ID cannot be empty")
    @Column(name = "donation_id", nullable = false)
    private Long donationId;
    
    @NotNull(message = "Distribution quantity cannot be empty")
    @Positive(message = "Distribution quantity must be greater than 0")
    @Column(name = "quantity_distributed", nullable = false)
    private Double quantityDistributed; // Distribution quantity
    
    @NotNull(message = "Recipient name cannot be empty")
    @Size(max = 100, message = "Recipient name length cannot exceed 100 characters")
    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName; // Recipient name
    
    @NotNull(message = "Recipient contact cannot be empty")
    @Size(max = 200, message = "Recipient contact length cannot exceed 200 characters")
    @Column(name = "recipient_contact", nullable = false, length = 200)
    private String recipientContact; // Recipient contact information
    
    @Size(max = 200, message = "Recipient information length cannot exceed 200 characters")
    @Column(name = "recipient_info", length = 200)
    private String recipientInfo; // Recipient information
    
    @Column(name = "distribution_date", nullable = false)
    private LocalDateTime distributionDate;
    
    @Column(columnDefinition = "TEXT")
    private String notes; // Notes
    
    // Association: Many distributions to one donation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id", insertable = false, updatable = false)
    private Donation donation;
    
    // Constructor
    public Distribution() {
        this.distributionDate = LocalDateTime.now();
    }
    
    public Distribution(Long donationId, Double quantityDistributed, String recipientInfo, String notes) {
        this();
        this.donationId = donationId;
        this.quantityDistributed = quantityDistributed;
        this.recipientInfo = recipientInfo;
        this.notes = notes;
    }
    
    // Getter and Setter methods
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
    
    public Donation getDonation() {
        return donation;
    }
    
    public void setDonation(Donation donation) {
        this.donation = donation;
    }
    
    @Override
    public String toString() {
        return "Distribution{" +
                "id=" + id +
                ", donationId=" + donationId +
                ", quantityDistributed=" + quantityDistributed +
                ", recipientInfo='" + recipientInfo + '\'' +
                ", distributionDate=" + distributionDate +
                ", notes='" + notes + '\'' +
                '}';
    }
}
