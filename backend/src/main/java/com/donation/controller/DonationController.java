package com.donation.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.donation.dto.DonationDTO;
import com.donation.service.DonationService;

import jakarta.validation.Valid;

/**
 * Donation Controller
 * Handles donation record related HTTP requests
 */
@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "http://localhost:3000") // allow frontend access
public class DonationController {
    
    @Autowired
    private DonationService donationService;
    
    /**
    * Get all donation records
     * @return donation record list
     */
    @GetMapping
    public ResponseEntity<List<DonationDTO>> getAllDonations() {
        List<DonationDTO> donations = donationService.getAllDonations();
        return ResponseEntity.ok(donations);
    }
    
    /**
    * Get donation record by ID
     * @param id donation record ID
     * @return donation record information
     */
    @GetMapping("/{id}")
    public ResponseEntity<DonationDTO> getDonationById(@PathVariable Long id) {
        Optional<DonationDTO> donation = donationService.getDonationById(id);
        return donation.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
    * Get donation records by donor ID
     * @param donorId donor ID
     * @return all donation records for the donor
     */
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<DonationDTO>> getDonationsByDonorId(@PathVariable Long donorId) {
        List<DonationDTO> donations = donationService.getDonationsByDonorId(donorId);
        return ResponseEntity.ok(donations);
    }
    
    /**
    * Get donation records by donation type
     * @param type donation type
     * @return all donation records for the donation type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<DonationDTO>> getDonationsByType(@PathVariable String type) {
        List<DonationDTO> donations = donationService.getDonationsByType(type);
        return ResponseEntity.ok(donations);
    }
    
    /**
    * Create new donation record
     * @param donationDTO donation record information
     * @return created donation record information
     */
    @PostMapping
    public ResponseEntity<DonationDTO> createDonation(@Valid @RequestBody DonationDTO donationDTO) {
        try {
            DonationDTO createdDonation = donationService.createDonation(donationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDonation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update donation record
     * @param id donation record ID
     * @param donationDTO updated donation record information
     * @return updated donation record information
     */
    @PutMapping("/{id}")
    public ResponseEntity<DonationDTO> updateDonation(@PathVariable Long id, 
                                                    @Valid @RequestBody DonationDTO donationDTO) {
        try {
            Optional<DonationDTO> updatedDonation = donationService.updateDonation(id, donationDTO);
            return updatedDonation.map(ResponseEntity::ok)
                                 .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete donation record
     * @param id donation record ID
     * @return deletion result
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        boolean deleted = donationService.deleteDonation(id);
        return deleted ? ResponseEntity.noContent().build() 
                      : ResponseEntity.notFound().build();
    }
    
    /**
    * Get total quantity for a donation type
     * @param type donation type
     * @return total quantity for the donation type
     */
    @GetMapping("/type/{type}/total")
    public ResponseEntity<Double> getTotalQuantityByType(@PathVariable String type) {
        Double totalQuantity = donationService.getTotalQuantityByType(type);
        return ResponseEntity.ok(totalQuantity);
    }
    
    /**
    * Get inventory summary for all donation types
     * @return inventory summary by donation type
     */
    @GetMapping("/inventory/summary")
    public ResponseEntity<List<Object[]>> getInventorySummary() {
        List<Object[]> summary = donationService.getInventorySummary();
        return ResponseEntity.ok(summary);
    }
}
