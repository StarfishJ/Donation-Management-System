package com.donation.dto;

/**
 * Inventory report data transfer object
 * Used for inventory report presentation
 */
public class InventoryReportDTO {
    
    private String donationType;  // donation type
    private Double totalReceived; // total received quantity
    private Double totalDistributed; // total distributed quantity
    private Double remainingQuantity; // remaining quantity
    private String unit; // unit
    
    // constructor
    public InventoryReportDTO() {}
    
    public InventoryReportDTO(String donationType, Double totalReceived, 
                             Double totalDistributed, Double remainingQuantity, String unit) {
        this.donationType = donationType;
        this.totalReceived = totalReceived;
        this.totalDistributed = totalDistributed;
        this.remainingQuantity = remainingQuantity;
        this.unit = unit;
    }
    
    // Getter and setter methods
    public String getDonationType() {
        return donationType;
    }
    
    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }
    
    public Double getTotalReceived() {
        return totalReceived;
    }
    
    public void setTotalReceived(Double totalReceived) {
        this.totalReceived = totalReceived;
    }
    
    public Double getTotalDistributed() {
        return totalDistributed;
    }
    
    public void setTotalDistributed(Double totalDistributed) {
        this.totalDistributed = totalDistributed;
    }
    
    public Double getRemainingQuantity() {
        return remainingQuantity;
    }
    
    public void setRemainingQuantity(Double remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    @Override
    public String toString() {
        return "InventoryReportDTO{" +
                "donationType='" + donationType + '\'' +
                ", totalReceived=" + totalReceived +
                ", totalDistributed=" + totalDistributed +
                ", remainingQuantity=" + remainingQuantity +
                ", unit='" + unit + '\'' +
                '}';
    }
}
