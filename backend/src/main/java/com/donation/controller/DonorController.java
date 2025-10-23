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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.donation.dto.DonorDTO;
import com.donation.service.DonorService;

import jakarta.validation.Valid;

/**
 * Donor Controller
 * Handles donor record related HTTP requests
 */
@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "http://localhost:3000") // allow frontend access
public class DonorController {
    
    @Autowired
    private DonorService donorService;
    
    /**
    *Get all donors
     * @return donor list
     */
    @GetMapping
    public ResponseEntity<List<DonorDTO>> getAllDonors() {
        List<DonorDTO> donors = donorService.getAllDonors();
        return ResponseEntity.ok(donors);
    }
    
    /**
    * Get donor by ID
     * @param id donor ID
     * @return donor information
     */
    @GetMapping("/{id}")
    public ResponseEntity<DonorDTO> getDonorById(@PathVariable Long id) {
        Optional<DonorDTO> donor = donorService.getDonorById(id);
        return donor.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    /**
    * Search donors by name
     * @param name donor name
     * @return list of donors matching the name
     */
    @GetMapping("/search")
    public ResponseEntity<List<DonorDTO>> searchDonorsByName(@RequestParam String name) {
        List<DonorDTO> donors = donorService.searchDonorsByName(name);
        return ResponseEntity.ok(donors);
    }
    
    /**
    * Create new donor
     * @param donorDTO donor information
     * @return created donor information
     */
    @PostMapping
    public ResponseEntity<DonorDTO> createDonor(@Valid @RequestBody DonorDTO donorDTO) {
        try {
            DonorDTO createdDonor = donorService.createDonor(donorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDonor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
    * Update donor information
     * @param id donor ID
     * @param donorDTO updated donor information
     * @return updated donor information
     */
    @PutMapping("/{id}")
    public ResponseEntity<DonorDTO> updateDonor(@PathVariable Long id, 
                                               @Valid @RequestBody DonorDTO donorDTO) {
        try {
            Optional<DonorDTO> updatedDonor = donorService.updateDonor(id, donorDTO);
            return updatedDonor.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete donor
     * @param id donor ID
     * @return deletion result
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonor(@PathVariable Long id) {
        boolean deleted = donorService.deleteDonor(id);
        return deleted ? ResponseEntity.noContent().build() 
                      : ResponseEntity.notFound().build();
    }
    
    /**
    * Get total number of donors
     * @return total number of donors
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalDonorsCount() {
        Long count = donorService.getTotalDonorsCount();
        return ResponseEntity.ok(count);
    }
}
