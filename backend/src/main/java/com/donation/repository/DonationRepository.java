package com.donation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.donation.entity.Donation;

/**
 * Donation Repository
 * Provides database operations for donation records
 */
@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    
    /**
    * Find donation records by donor ID
     * @param donorId donor ID
     * @return all donations for the donor
     */
    List<Donation> findByDonorId(Long donorId);
    
    /**
    * Find donation records by donation type
     * @param donationType donation type
     * @return all donations for the donation type
     */
    List<Donation> findByDonationType(String donationType);
    
    /**
    * Find donation records by date range
     * @param startDate start date
     * @param endDate end date
     * @return donations within the specified date range
     */
    List<Donation> findByDonationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
    * Get total quantity for a donation type
     * @param donationType donation type
     * @return total quantity for the donation type
     */
    @Query("SELECT COALESCE(SUM(d.quantity), 0) FROM Donation d WHERE d.donationType = :donationType")
    Double getTotalQuantityByType(@Param("donationType") String donationType);
    
    /**
    * Get inventory summary for all donation types
     * @return inventory summary by donation type
     */
    @Query("SELECT d.donationType, SUM(d.quantity) as totalQuantity, d.unit " +
           "FROM Donation d GROUP BY d.donationType, d.unit ORDER BY totalQuantity DESC")
    List<Object[]> getInventorySummary();
    
    /**
    * Get total donation value for a donor
     * @param donorId donor ID
     * @return total donation value for the donor
     */
    @Query("SELECT COALESCE(SUM(d.quantity), 0) FROM Donation d WHERE d.donorId = :donorId")
    Double getTotalDonationValueByDonor(@Param("donorId") Long donorId);
    
    /**
     * Find donations with distribution records
     * @param donationId donation record ID
     * @return donations with distribution records
     */
    @Query("SELECT d FROM Donation d LEFT JOIN FETCH d.distributions WHERE d.id = :donationId")
    Donation findByIdWithDistributions(@Param("donationId") Long donationId);
}
