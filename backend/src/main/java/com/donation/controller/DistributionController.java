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

import com.donation.dto.DistributionDTO;
import com.donation.service.DistributionService;

import jakarta.validation.Valid;

/**
 * Distribution Controller
 * Handles distribution record related HTTP requests
 */
@RestController
@RequestMapping("/api/distributions")
@CrossOrigin(origins = "http://localhost:3000") // allow frontend access
public class DistributionController {
    
    @Autowired
    private DistributionService distributionService;
    
    /**
     * Get all distribution records
     * @return distribution record list
     */
    @GetMapping
    public ResponseEntity<List<DistributionDTO>> getAllDistributions() {
        List<DistributionDTO> distributions = distributionService.getAllDistributions();
        return ResponseEntity.ok(distributions);
    }
    
    /**
     * Get distribution record by ID
     * @param id distribution record ID
     * @return distribution record information
     */
    @GetMapping("/{id}")
    public ResponseEntity<DistributionDTO> getDistributionById(@PathVariable Long id) {
        Optional<DistributionDTO> distribution = distributionService.getDistributionById(id);
        return distribution.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }
    
    /**
    * Get distribution records by donation ID
     * @param donationId donation record ID
     * @return all distribution records for the donation
     */
    @GetMapping("/donation/{donationId}")
    public ResponseEntity<List<DistributionDTO>> getDistributionsByDonationId(@PathVariable Long donationId) {
        List<DistributionDTO> distributions = distributionService.getDistributionsByDonationId(donationId);
        return ResponseEntity.ok(distributions);
    }
    
    /**
     * Create new distribution record
     * @param distributionDTO distribution record information
     * @return created distribution record information
     */
    @PostMapping
    public ResponseEntity<DistributionDTO> createDistribution(@Valid @RequestBody DistributionDTO distributionDTO) {
        try {
            DistributionDTO createdDistribution = distributionService.createDistribution(distributionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDistribution);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update distribution record
     * @param id distribution record ID
     * @param distributionDTO updated distribution record information
     * @return updated distribution record information
     */
    @PutMapping("/{id}")
    public ResponseEntity<DistributionDTO> updateDistribution(@PathVariable Long id, 
                                                            @Valid @RequestBody DistributionDTO distributionDTO) {
        try {
            Optional<DistributionDTO> updatedDistribution = distributionService.updateDistribution(id, distributionDTO);
            return updatedDistribution.map(ResponseEntity::ok)
                                     .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete distribution record
     * @param id distribution record ID
     * @return deletion result
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistribution(@PathVariable Long id) {
        boolean deleted = distributionService.deleteDistribution(id);
        return deleted ? ResponseEntity.noContent().build() 
                      : ResponseEntity.notFound().build();
    }
    
    /**
    * Get total distributed quantity for a donation
     * @param donationId donation record ID
     * @return total distributed quantity for the donation
     */
    @GetMapping("/donation/{donationId}/total")
    public ResponseEntity<Double> getTotalDistributedQuantity(@PathVariable Long donationId) {
        Double totalQuantity = distributionService.getTotalDistributedQuantity(donationId);
        return ResponseEntity.ok(totalQuantity);
    }
    
    /**
    * Get remaining quantity for a donation
     * @param donationId donation record ID
     * @return remaining quantity for the donation
     */
    @GetMapping("/donation/{donationId}/remaining")
    public ResponseEntity<Double> getRemainingQuantity(@PathVariable Long donationId) {
        Double remainingQuantity = distributionService.getRemainingQuantity(donationId);
        return ResponseEntity.ok(remainingQuantity);
    }
    
    /**
    * Get summary statistics for all distribution records
     * @return distribution summary information
     */
    @GetMapping("/summary")
    public ResponseEntity<Object[]> getDistributionSummary() {
        Object[] summary = distributionService.getDistributionSummary();
        return ResponseEntity.ok(summary);
    }
}
