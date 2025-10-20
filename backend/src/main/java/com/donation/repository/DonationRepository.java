package com.donation.repository;

import com.donation.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 捐赠记录数据访问层
 * 提供捐赠记录相关的数据库操作
 */
@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    
    /**
     * 根据捐赠者ID查找捐赠记录
     * @param donorId 捐赠者ID
     * @return 该捐赠者的所有捐赠记录
     */
    List<Donation> findByDonorId(Long donorId);
    
    /**
     * 根据捐赠类型查找捐赠记录
     * @param donationType 捐赠类型
     * @return 该类型的所有捐赠记录
     */
    List<Donation> findByDonationType(String donationType);
    
    /**
     * 根据日期范围查找捐赠记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 指定日期范围内的捐赠记录
     */
    List<Donation> findByDonationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 获取指定捐赠类型的总数量
     * @param donationType 捐赠类型
     * @return 该类型的总数量
     */
    @Query("SELECT COALESCE(SUM(d.quantity), 0) FROM Donation d WHERE d.donationType = :donationType")
    Double getTotalQuantityByType(@Param("donationType") String donationType);
    
    /**
     * 获取所有捐赠类型的库存汇总
     * @return 按类型分组的库存统计
     */
    @Query("SELECT d.donationType, SUM(d.quantity) as totalQuantity, d.unit " +
           "FROM Donation d GROUP BY d.donationType, d.unit ORDER BY totalQuantity DESC")
    List<Object[]> getInventorySummary();
    
    /**
     * 获取指定捐赠者的总捐赠价值
     * @param donorId 捐赠者ID
     * @return 该捐赠者的总捐赠价值
     */
    @Query("SELECT COALESCE(SUM(d.quantity), 0) FROM Donation d WHERE d.donorId = :donorId")
    Double getTotalDonationValueByDonor(@Param("donorId") Long donorId);
    
    /**
     * 查找包含分发记录的捐赠
     * @param donationId 捐赠记录ID
     * @return 包含分发记录的捐赠对象
     */
    @Query("SELECT d FROM Donation d LEFT JOIN FETCH d.distributions WHERE d.id = :donationId")
    Donation findByIdWithDistributions(@Param("donationId") Long donationId);
}
