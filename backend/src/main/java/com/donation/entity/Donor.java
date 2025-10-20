package com.donation.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Donor Entity Class
 * Represents donor information in the system
 */
@Entity
@Table(name = "donors")
public class Donor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Donor name cannot be empty")
    @Size(max = 100, message = "Name length cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;
    
    @Size(max = 200, message = "Contact information length cannot exceed 200 characters")
    @Column(name = "contact_info", length = 200)
    private String contactInfo;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // Association: A donor can have multiple donation records
    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Donation> donations;
    
    // Constructor
    public Donor() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Donor(String name, String contactInfo) {
        this();
        this.name = name;
        this.contactInfo = contactInfo;
    }
    
    // Getter and Setter methods
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
    
    public List<Donation> getDonations() {
        return donations;
    }
    
    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }
    
    @Override
    public String toString() {
        return "Donor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
