package com.donation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 分发记录数据传输对象
 * 用于前后端数据传输
 */
public class DistributionDTO {
    
    private Long id;
    
    @NotNull(message = "捐赠记录ID不能为空")
    private Long donationId;
    
    @NotNull(message = "分发数量不能为空")
    @Positive(message = "分发数量必须大于0")
    private Double quantityDistributed;
    
    @NotNull(message = "接收者姓名不能为空")
    @Size(max = 100, message = "接收者姓名长度不能超过100个字符")
    private String recipientName;
    
    @NotNull(message = "接收者联系方式不能为空")
    @Size(max = 200, message = "接收者联系方式长度不能超过200个字符")
    private String recipientContact;
    
    @Size(max = 200, message = "接收者信息长度不能超过200个字符")
    private String recipientInfo;
    
    private LocalDateTime distributionDate;
    
    private String notes;
    
    // 关联信息
    private String donationType;
    private String donorName;
    
    // 构造函数
    public DistributionDTO() {}
    
    public DistributionDTO(Long donationId, Double quantityDistributed, String recipientInfo, String notes) {
        this.donationId = donationId;
        this.quantityDistributed = quantityDistributed;
        this.recipientInfo = recipientInfo;
        this.notes = notes;
    }
    
    // Getter 和 Setter 方法
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
