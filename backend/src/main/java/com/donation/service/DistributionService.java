package com.donation.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.donation.dto.DistributionDTO;
import com.donation.entity.Distribution;
import com.donation.entity.Donation;
import com.donation.repository.DistributionRepository;
import com.donation.repository.DonationRepository;

/**
 * Distribution Service
 * Handles business logic for distribution records
 */
@Service
@Transactional
public class DistributionService {
    
    @Autowired
    private DistributionRepository distributionRepository;
    
    @Autowired
    private DonationRepository donationRepository;
    
    /**
     * Get all distribution records
     * @return distribution record list
     */
    @Transactional(readOnly = true)
    public List<DistributionDTO> getAllDistributions() {
        return distributionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get distribution record by ID
     * @param id distribution record ID
     * @return distribution record information
     */
    @Transactional(readOnly = true)
    public Optional<DistributionDTO> getDistributionById(Long id) {
        return distributionRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    /**
     * Get distribution records by donation ID
     * @param donationId donation record ID
     * @return all distribution records for the donation
     */
    @Transactional(readOnly = true)
    public List<DistributionDTO> getDistributionsByDonationId(Long donationId) {
        return distributionRepository.findByDonationId(donationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Create new distribution record
     * @param distributionDTO distribution record information
     * @return created distribution record information
     */
    public DistributionDTO createDistribution(DistributionDTO distributionDTO) {
        // Validate donation exists
        Donation donation = donationRepository.findById(distributionDTO.getDonationId())
                .orElseThrow(() -> new RuntimeException("Donation record not found: " + distributionDTO.getDonationId()));
        
        // Check remaining quantity
        Double remainingQuantity = distributionRepository.getRemainingQuantity(distributionDTO.getDonationId());
        if (distributionDTO.getQuantityDistributed() > remainingQuantity) {
            throw new RuntimeException("Distributed quantity exceeds remaining. Remaining: " + remainingQuantity);
        }
        
        Distribution distribution = convertToEntity(distributionDTO);
        Distribution savedDistribution = distributionRepository.save(distribution);
        return convertToDTO(savedDistribution);
    }
    
    /**
     * Update distribution record
     * @param id distribution record ID
     * @param distributionDTO updated distribution record information
     * @return updated distribution record information
     */
    public Optional<DistributionDTO> updateDistribution(Long id, DistributionDTO distributionDTO) {
        return distributionRepository.findById(id)
                .map(existingDistribution -> {
                    existingDistribution.setQuantityDistributed(distributionDTO.getQuantityDistributed());
                    existingDistribution.setRecipientInfo(distributionDTO.getRecipientInfo());
                    existingDistribution.setNotes(distributionDTO.getNotes());
                    Distribution updatedDistribution = distributionRepository.save(existingDistribution);
                    return convertToDTO(updatedDistribution);
                });
    }
    
    /**
     * Delete distribution record
     * @param id distribution record ID
     * @return whether the deletion was successful
     */
    public boolean deleteDistribution(Long id) {
        if (distributionRepository.existsById(id)) {
            distributionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Get total distributed quantity for a donation
     * @param donationId donation record ID
     * @return total distributed quantity
     */
    @Transactional(readOnly = true)
    public Double getTotalDistributedQuantity(Long donationId) {
        return distributionRepository.getTotalDistributedQuantity(donationId);
    }
    
    /**
     * Get remaining quantity for a donation
     * @param donationId donation record ID
     * @return remaining quantity
     */
    @Transactional(readOnly = true)
    public Double getRemainingQuantity(Long donationId) {
        return distributionRepository.getRemainingQuantity(donationId);
    }
    
    /**
     * Get summary statistics for all distribution records
     * @return distribution summary information
     */
    @Transactional(readOnly = true)
    public Object[] getDistributionSummary() {
        return distributionRepository.getDistributionSummary();
    }
    
    /**
     * Convert entity to DTO
     * @param distribution distribution record entity
     * @return distribution record DTO
     */
    private DistributionDTO convertToDTO(Distribution distribution) {
        DistributionDTO dto = new DistributionDTO();
        dto.setId(distribution.getId());
        dto.setDonationId(distribution.getDonationId());
        dto.setQuantityDistributed(distribution.getQuantityDistributed());
        dto.setRecipientName(distribution.getRecipientName());
        dto.setRecipientContact(distribution.getRecipientContact());
        dto.setRecipientInfo(distribution.getRecipientInfo());
        dto.setDistributionDate(distribution.getDistributionDate());
        dto.setNotes(distribution.getNotes());
        
        // Set associated information
        if (distribution.getDonation() != null) {
            dto.setDonationType(distribution.getDonation().getDonationType());
            if (distribution.getDonation().getDonor() != null) {
                dto.setDonorName(distribution.getDonation().getDonor().getName());
            }
        }
        
        return dto;
    }
    
    /**
     * Convert DTO to entity
     * @param distributionDTO distribution record DTO
     * @return distribution record entity
     */
    private Distribution convertToEntity(DistributionDTO distributionDTO) {
        Distribution distribution = new Distribution();
        distribution.setId(distributionDTO.getId());
        distribution.setDonationId(distributionDTO.getDonationId());
        distribution.setQuantityDistributed(distributionDTO.getQuantityDistributed());
        distribution.setRecipientName(distributionDTO.getRecipientName());
        distribution.setRecipientContact(distributionDTO.getRecipientContact());
        distribution.setRecipientInfo(distributionDTO.getRecipientInfo());
        distribution.setNotes(distributionDTO.getNotes());
        return distribution;
    }
}
