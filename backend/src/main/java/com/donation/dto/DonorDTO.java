package com.donation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 捐赠者数据传输对象
 * 用于前后端数据传输
 */
public class DonorDTO {
    
    private Long id;
    
    @NotBlank(message = "捐赠者姓名不能为空")
    @Size(max = 100, message = "姓名长度不能超过100个字符")
    private String name;
    
    @Size(max = 200, message = "联系信息长度不能超过200个字符")
    private String contactInfo;
    
    private LocalDateTime createdAt;
    
    private List<DonationDTO> donations;
    
    // 构造函数
    public DonorDTO() {}
    
    public DonorDTO(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }
    
    // Getter 和 Setter 方法
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
