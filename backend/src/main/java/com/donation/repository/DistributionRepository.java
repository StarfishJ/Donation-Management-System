package com.donation.repository;

import com.donation.entity.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分发记录数据访问层
 * 提供分发记录相关的数据库操作
 */
@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long> {
    
    /**
     * 根据捐赠记录ID查找分发记录
     * @param donationId 捐赠记录ID
     * @return 该捐赠记录的所有分发记录
     */
    List<Distribution> findByDonationId(Long donationId);
    
    /**
     * 根据日期范围查找分发记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 指定日期范围内的分发记录
     */
    List<Distribution> findByDistributionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 获取指定捐赠记录的总分发数量
     * @param donationId 捐赠记录ID
     * @return 该捐赠记录的总分发数量
     */
    @Query("SELECT COALESCE(SUM(d.quantityDistributed), 0) FROM Distribution d WHERE d.donationId = :donationId")
    Double getTotalDistributedQuantity(@Param("donationId") Long donationId);
    
    /**
     * 获取指定捐赠记录的剩余数量
     * @param donationId 捐赠记录ID
     * @return 该捐赠记录的剩余数量
     */
    @Query("SELECT " +
           "(SELECT COALESCE(d.quantity, 0) FROM Donation d WHERE d.id = :donationId) - " +
           "COALESCE((SELECT SUM(dist.quantityDistributed) FROM Distribution dist WHERE dist.donationId = :donationId), 0)")
    Double getRemainingQuantity(@Param("donationId") Long donationId);
    
    /**
     * 获取所有分发记录的汇总统计
     * @return 分发统计信息
     */
    @Query("SELECT COUNT(d), SUM(d.quantityDistributed) FROM Distribution d")
    Object[] getDistributionSummary();
    
    /**
     * 根据接收者信息查找分发记录
     * @param recipientInfo 接收者信息
     * @return 匹配的分发记录列表
     */
    List<Distribution> findByRecipientInfoContainingIgnoreCase(String recipientInfo);
}
