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
 * Donor Repository
 * Provides database operations for donor records
 */
@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    
    /**
    * Find donors by name (fuzzy search)
     * @param name donor name
     * @return matching donor list
     */
    List<Donor> findByNameContainingIgnoreCase(String name);
    
    /**
    * Find donors by name (exact)
     * @param name donor name
     * @return matching donor
     */
    Optional<Donor> findByName(String name);
    
    /**
    * Get total number of donors
     * @return total number of donors
     */
    @Query("SELECT COUNT(d) FROM Donor d")
    Long countTotalDonors();

    /**
     * Count donors created after the given time (e.g., for recent/new donors)
     */
    Long countByCreatedAtAfter(LocalDateTime since);
    
    /**
    * Get all donations for a donor
     * @param donorId donor ID
     * @return donor information and its donations
     */
    @Query("SELECT d FROM Donor d LEFT JOIN FETCH d.donations WHERE d.id = :donorId")
    Optional<Donor> findByIdWithDonations(@Param("donorId") Long donorId);
}
