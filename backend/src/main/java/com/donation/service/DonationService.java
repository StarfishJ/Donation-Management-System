package com.donation.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.donation.dto.DonationDTO;
import com.donation.entity.Donation;
import com.donation.entity.Donor;
import com.donation.repository.DonationRepository;
import com.donation.repository.DonorRepository;

/**
 * Donation Service Layer
 * Handles business logic for donation records
 */
@Service
@Transactional
public class DonationService {
    
    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private DonorRepository donorRepository;
    
    /**
     * Get all donation records
     * @return donation record list
     */
    @Transactional(readOnly = true)
    public List<DonationDTO> getAllDonations() {
        return donationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get donation record by ID
     * @param id donation record ID
     * @return donation record information
     */
    @Transactional(readOnly = true)
    public Optional<DonationDTO> getDonationById(Long id) {
        return donationRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    /**
     * Get donation records by donor ID 
     * @param donorId donor ID
     * @return all donation records for the donor
     */
    @Transactional(readOnly = true)
    public List<DonationDTO> getDonationsByDonorId(Long donorId) {
        return donationRepository.findByDonorId(donorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get donation records by donation type
     * @param donationType donation type
     * @return all donation records for the donation type
     */
    @Transactional(readOnly = true)
    public List<DonationDTO> getDonationsByType(String donationType) {
        return donationRepository.findByDonationType(donationType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Create new donation record
     * @param donationDTO donation record information
     * @return created donation record information
     */
    public DonationDTO createDonation(DonationDTO donationDTO) {
        // Validate donor exists
        Donor donor = donorRepository.findById(donationDTO.getDonorId())
                .orElseThrow(() -> new RuntimeException("Donor not found: " + donationDTO.getDonorId()));
        
        Donation donation = convertToEntity(donationDTO);
        Donation savedDonation = donationRepository.save(donation);
        return convertToDTO(savedDonation);
    }
    
    /**
     * Update donation record
     * @param id donation record ID
     * @param donationDTO updated donation record information
     * @return updated donation record information
     */
    public Optional<DonationDTO> updateDonation(Long id, DonationDTO donationDTO) {
        return donationRepository.findById(id)
                .map(existingDonation -> {
                    existingDonation.setDonationType(donationDTO.getDonationType());
                    existingDonation.setQuantity(donationDTO.getQuantity());
                    existingDonation.setUnit(donationDTO.getUnit());
                    existingDonation.setDescription(donationDTO.getDescription());
                    Donation updatedDonation = donationRepository.save(existingDonation);
                    return convertToDTO(updatedDonation);
                });
    }
    
    /**
     * Delete donation record
     * @param id donation record ID
     * @return whether the deletion was successful
     */
    public boolean deleteDonation(Long id) {
        if (donationRepository.existsById(id)) {
            donationRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Get total quantity for a donation type
     * @param donationType donation type
     * @return total quantity for the donation type
     */
    @Transactional(readOnly = true)
    public Double getTotalQuantityByType(String donationType) {
        return donationRepository.getTotalQuantityByType(donationType);
    }
    
    /**
     * Get inventory summary for all donation types
     * @return inventory summary by donation type
     */
    @Transactional(readOnly = true)
    public List<Object[]> getInventorySummary() {
        return donationRepository.getInventorySummary();
    }
    
    /**
     * Convert entity to DTO
     * @param donation donation record entity
     * @return donation record DTO
     */
    private DonationDTO convertToDTO(Donation donation) {
        DonationDTO dto = new DonationDTO();
        dto.setId(donation.getId());
        dto.setDonorId(donation.getDonorId());
        dto.setDonationType(donation.getDonationType());
        dto.setQuantity(donation.getQuantity());
        dto.setUnit(donation.getUnit());
        dto.setDescription(donation.getDescription());
        dto.setDonationDate(donation.getDonationDate());
        
        // Set donor name
        if (donation.getDonor() != null) {
            dto.setDonorName(donation.getDonor().getName());
        }
        
        return dto;
    }
    
    /**
     * Convert DTO to entity
     * @param donationDTO donation record DTO
     * @return donation record entity
     */
    private Donation convertToEntity(DonationDTO donationDTO) {
        Donation donation = new Donation();
        donation.setId(donationDTO.getId());
        donation.setDonorId(donationDTO.getDonorId());
        donation.setDonationType(donationDTO.getDonationType());
        donation.setQuantity(donationDTO.getQuantity());
        donation.setUnit(donationDTO.getUnit());
        donation.setDescription(donationDTO.getDescription());
        return donation;
    }
}
