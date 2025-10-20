package com.donation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Donation Entity Class
 * Represents donation information in the system
 */
@Entity
@Table(name = "donations")
public class Donation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Donor ID cannot be empty")
    @Column(name = "donor_id", nullable = false)
    private Long donorId;
    
    @NotBlank(message = "Donation type cannot be empty")
    @Size(max = 50, message = "Donation type length cannot exceed 50 characters")
    @Column(name = "donation_type", nullable = false, length = 50)
    private String donationType; // Donation type: money, food, clothing, etc.
    
    @NotNull(message = "Donation quantity cannot be empty")
    @Positive(message = "Donation quantity must be greater than 0")
    @Column(nullable = false)
    private Double quantity; // Quantity or amount
    
    @Size(max = 20, message = "Unit length cannot exceed 20 characters")
    @Column(length = 20)
    private String unit = "pieces"; // Unit: dollars, pieces, kg, etc.
    
    @Column(columnDefinition = "TEXT")
    private String description; // Description
    
    @Column(name = "donation_date", nullable = false)
    private LocalDateTime donationDate;
    
    // Association: Many donations to one donor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", insertable = false, updatable = false)
    private Donor donor;
    
    // Association: One donation to many distributions
    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Distribution> distributions;
    
    // Constructor
    public Donation() {
        this.donationDate = LocalDateTime.now();
    }
    
    public Donation(Long donorId, String donationType, Double quantity, String unit, String description) {
        this();
        this.donorId = donorId;
        this.donationType = donationType;
        this.quantity = quantity;
        this.unit = unit;
        this.description = description;
    }
    
    // Getter and Setter methods
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
    
    public Donor getDonor() {
        return donor;
    }
    
    public void setDonor(Donor donor) {
        this.donor = donor;
    }
    
    public List<Distribution> getDistributions() {
        return distributions;
    }
    
    public void setDistributions(List<Distribution> distributions) {
        this.distributions = distributions;
    }
    
    @Override
    public String toString() {
        return "Donation{" +
                "id=" + id +
                ", donorId=" + donorId +
                ", donationType='" + donationType + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", description='" + description + '\'' +
                ", donationDate=" + donationDate +
                '}';
    }
}
