package com.donation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.donation.entity.Donor;

/**
 * 捐赠者数据访问层
 * 提供捐赠者相关的数据库操作
 */
@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    
    /**
     * 根据姓名查找捐赠者（模糊查询）
     * @param name 捐赠者姓名
     * @return 匹配的捐赠者列表
     */
    List<Donor> findByNameContainingIgnoreCase(String name);
    
    /**
     * 根据姓名精确查找捐赠者
     * @param name 捐赠者姓名
     * @return 匹配的捐赠者
     */
    Optional<Donor> findByName(String name);
    
    /**
     * 获取捐赠者总数
     * @return 捐赠者总数
     */
    @Query("SELECT COUNT(d) FROM Donor d")
    Long countTotalDonors();

    /**
     * Count donors created after the given time (e.g., for recent/new donors)
     */
    Long countByCreatedAtAfter(LocalDateTime since);
    
    /**
     * 根据捐赠者ID获取其所有捐赠记录
     * @param donorId 捐赠者ID
     * @return 捐赠者信息及其捐赠记录
     */
    @Query("SELECT d FROM Donor d LEFT JOIN FETCH d.donations WHERE d.id = :donorId")
    Optional<Donor> findByIdWithDonations(@Param("donorId") Long donorId);
}
