package com.donation.dto;

/**
 * 库存报告数据传输对象
 * 用于库存报告的展示
 */
public class InventoryReportDTO {
    
    private String donationType;  // 捐赠类型
    private Double totalReceived; // 总接收数量
    private Double totalDistributed; // 总分发数量
    private Double remainingQuantity; // 剩余数量
    private String unit; // 单位
    
    // 构造函数
    public InventoryReportDTO() {}
    
    public InventoryReportDTO(String donationType, Double totalReceived, 
                             Double totalDistributed, Double remainingQuantity, String unit) {
        this.donationType = donationType;
        this.totalReceived = totalReceived;
        this.totalDistributed = totalDistributed;
        this.remainingQuantity = remainingQuantity;
        this.unit = unit;
    }
    
    // Getter 和 Setter 方法
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
