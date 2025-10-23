package com.donation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.donation.entity.Distribution;

/**
 * Distribution Repository
 * Provides database operations for distribution records
 */
@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long> {
    
    /**
     * Find distribution records by donation record ID
     * @param donationId donation record ID
     * @return all distribution records for the donation
     */
    List<Distribution> findByDonationId(Long donationId);
    
    /**
    * Find distribution records by date range
     * @param startDate start date
     * @param endDate end date
     * @return distribution records within the specified date range
     */
    List<Distribution> findByDistributionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
    * Get total distributed quantity for a donation
     * @param donationId donation record ID
     * @return total distributed quantity for the donation
     */
    @Query("SELECT COALESCE(SUM(d.quantityDistributed), 0) FROM Distribution d WHERE d.donationId = :donationId")
    Double getTotalDistributedQuantity(@Param("donationId") Long donationId);
    
    /**
    * Get remaining quantity for a donation
     * @param donationId donation record ID
     * @return remaining quantity for the donation
     */
    @Query("SELECT " +
           "(SELECT COALESCE(d.quantity, 0) FROM Donation d WHERE d.id = :donationId) - " +
           "COALESCE((SELECT SUM(dist.quantityDistributed) FROM Distribution dist WHERE dist.donationId = :donationId), 0)")
    Double getRemainingQuantity(@Param("donationId") Long donationId);
    
    /**
    * Get summary statistics for all distribution records
     * @return distribution summary information
     */
    @Query("SELECT COUNT(d), SUM(d.quantityDistributed) FROM Distribution d")
    Object[] getDistributionSummary();
    
    /**
    * Find distribution records by recipient information
     * @param recipientInfo recipient information
     * @return matching distribution records list
     */
    List<Distribution> findByRecipientInfoContainingIgnoreCase(String recipientInfo);
}
