package com.donation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 捐赠记录实体类
 * 表示系统中的捐赠记录信息
 */
@Entity
@Table(name = "donations")
public class Donation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "捐赠者ID不能为空")
    @Column(name = "donor_id", nullable = false)
    private Long donorId;
    
    @NotBlank(message = "捐赠类型不能为空")
    @Size(max = 50, message = "捐赠类型长度不能超过50个字符")
    @Column(name = "donation_type", nullable = false, length = 50)
    private String donationType; // 捐赠类型：money, food, clothing, etc.
    
    @NotNull(message = "捐赠数量不能为空")
    @Positive(message = "捐赠数量必须大于0")
    @Column(nullable = false)
    private Double quantity; // 数量或金额
    
    @Size(max = 20, message = "单位长度不能超过20个字符")
    @Column(length = 20)
    private String unit = "pieces"; // 单位：元、件、公斤等
    
    @Column(columnDefinition = "TEXT")
    private String description; // 详细描述
    
    @Column(name = "donation_date", nullable = false)
    private LocalDateTime donationDate;
    
    // 关联关系：多对一，多个捐赠记录对应一个捐赠者
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", insertable = false, updatable = false)
    private Donor donor;
    
    // 关联关系：一对多，一个捐赠记录可以有多个分发记录
    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Distribution> distributions;
    
    // 构造函数
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
