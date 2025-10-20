package com.donation.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 捐赠者报告数据传输对象
 * 用于捐赠者报告的展示
 */
public class DonorReportDTO {
    
    private Long donorId;           // 捐赠者ID
    private String donorName;       // 捐赠者姓名
    private String contactInfo;     // 联系信息
    private LocalDateTime firstDonationDate; // 首次捐赠日期
    private LocalDateTime lastDonationDate;  // 最近捐赠日期
    private Integer totalDonations; // 总捐赠次数
    private Double totalValue;      // 总捐赠价值
    private List<DonationSummaryDTO> donationSummaries; // 捐赠汇总
    
    // 构造函数
    public DonorReportDTO() {}
    
    public DonorReportDTO(Long donorId, String donorName, String contactInfo,
                         LocalDateTime firstDonationDate, LocalDateTime lastDonationDate,
                         Integer totalDonations, Double totalValue) {
        this.donorId = donorId;
        this.donorName = donorName;
        this.contactInfo = contactInfo;
        this.firstDonationDate = firstDonationDate;
        this.lastDonationDate = lastDonationDate;
        this.totalDonations = totalDonations;
        this.totalValue = totalValue;
    }
    
    // Getter 和 Setter 方法
    public Long getDonorId() {
        return donorId;
    }
    
    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }
    
    public String getDonorName() {
        return donorName;
    }
    
    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public LocalDateTime getFirstDonationDate() {
        return firstDonationDate;
    }
    
    public void setFirstDonationDate(LocalDateTime firstDonationDate) {
        this.firstDonationDate = firstDonationDate;
    }
    
    public LocalDateTime getLastDonationDate() {
        return lastDonationDate;
    }
    
    public void setLastDonationDate(LocalDateTime lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }
    
    public Integer getTotalDonations() {
        return totalDonations;
    }
    
    public void setTotalDonations(Integer totalDonations) {
        this.totalDonations = totalDonations;
    }
    
    public Double getTotalValue() {
        return totalValue;
    }
    
    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }
    
    public List<DonationSummaryDTO> getDonationSummaries() {
        return donationSummaries;
    }
    
    public void setDonationSummaries(List<DonationSummaryDTO> donationSummaries) {
        this.donationSummaries = donationSummaries;
    }
    
    @Override
    public String toString() {
        return "DonorReportDTO{" +
                "donorId=" + donorId +
                ", donorName='" + donorName + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", firstDonationDate=" + firstDonationDate +
                ", lastDonationDate=" + lastDonationDate +
                ", totalDonations=" + totalDonations +
                ", totalValue=" + totalValue +
                '}';
    }
    
    /**
     * 捐赠汇总内部类
     */
    public static class DonationSummaryDTO {
        private String donationType;
        private Integer count;
        private Double totalQuantity;
        private String unit;
        
        // 构造函数
        public DonationSummaryDTO() {}
        
        public DonationSummaryDTO(String donationType, Integer count, Double totalQuantity, String unit) {
            this.donationType = donationType;
            this.count = count;
            this.totalQuantity = totalQuantity;
            this.unit = unit;
        }
        
        // Getter 和 Setter 方法
        public String getDonationType() {
            return donationType;
        }
        
        public void setDonationType(String donationType) {
            this.donationType = donationType;
        }
        
        public Integer getCount() {
            return count;
        }
        
        public void setCount(Integer count) {
            this.count = count;
        }
        
        public Double getTotalQuantity() {
            return totalQuantity;
        }
        
        public void setTotalQuantity(Double totalQuantity) {
            this.totalQuantity = totalQuantity;
        }
        
        public String getUnit() {
            return unit;
        }
        
        public void setUnit(String unit) {
            this.unit = unit;
        }
        
        @Override
        public String toString() {
            return "DonationSummaryDTO{" +
                    "donationType='" + donationType + '\'' +
                    ", count=" + count +
                    ", totalQuantity=" + totalQuantity +
                    ", unit='" + unit + '\'' +
                    '}';
        }
    }
}
