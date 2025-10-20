package com.donation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 捐赠记录数据传输对象
 * 用于前后端数据传输
 */
public class DonationDTO {
    
    private Long id;
    
    @NotNull(message = "捐赠者ID不能为空")
    private Long donorId;
    
    @NotBlank(message = "捐赠类型不能为空")
    @Size(max = 50, message = "捐赠类型长度不能超过50个字符")
    private String donationType;
    
    @NotNull(message = "捐赠数量不能为空")
    @Positive(message = "捐赠数量必须大于0")
    private Double quantity;
    
    @Size(max = 20, message = "单位长度不能超过20个字符")
    private String unit = "pieces";
    
    private String description;
    
    private LocalDateTime donationDate;
    
    // 关联信息
    private String donorName;
    private List<DistributionDTO> distributions;
    
    // 构造函数
    public DonationDTO() {}
    
    public DonationDTO(Long donorId, String donationType, Double quantity, String unit, String description) {
        this.donorId = donorId;
        this.donationType = donationType;
        this.quantity = quantity;
        this.unit = unit;
        this.description = description;
    }
    
    // Getter 和 Setter 方法
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
