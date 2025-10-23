package com.donation.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.donation.dto.DonorDTO;
import com.donation.entity.Donor;
import com.donation.repository.DonorRepository;

/**
 * Donor Service Layer
 * Handles business logic for donor records
 */
@Service
@Transactional
public class DonorService {
    
    @Autowired
    private DonorRepository donorRepository;
    
    /**
     * Get all donors
     * @return donor list
     */
    @Transactional(readOnly = true)
    public List<DonorDTO> getAllDonors() {
        return donorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get donor by ID
     * @param id donor ID
     * @return donor information
     */
    @Transactional(readOnly = true)
    public Optional<DonorDTO> getDonorById(Long id) {
        return donorRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    /**
     * Search donors by name
     * @param name donor name
     * @return list of donors matching the name
     */
    @Transactional(readOnly = true)
    public List<DonorDTO> searchDonorsByName(String name) {
        return donorRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Create new donor
     * @param donorDTO donor information
     * @return created donor information
     */
    public DonorDTO createDonor(DonorDTO donorDTO) {
        Donor donor = convertToEntity(donorDTO);
        Donor savedDonor = donorRepository.save(donor);
        return convertToDTO(savedDonor);
    }
    
    /**
     * Update donor information
     * @param id donor ID
     * @param donorDTO updated donor information
     * @return updated donor information
     */
    public Optional<DonorDTO> updateDonor(Long id, DonorDTO donorDTO) {
        return donorRepository.findById(id)
                .map(existingDonor -> {
                    existingDonor.setName(donorDTO.getName());
                    existingDonor.setContactInfo(donorDTO.getContactInfo());
                    Donor updatedDonor = donorRepository.save(existingDonor);
                    return convertToDTO(updatedDonor);
                });
    }
    
    /**
     * Delete donor
     * @param id donor ID
     * @return 是否删除成功
     */
    public boolean deleteDonor(Long id) {
        if (donorRepository.existsById(id)) {
            donorRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Get total number of donors
     * @return total number of donors
     */
    @Transactional(readOnly = true)
    public Long getTotalDonorsCount() {
        return donorRepository.countTotalDonors();
    }
    
    /**
     * Convert entity to DTO
     * @param donor donor entity
     * @return donor DTO
     */
    private DonorDTO convertToDTO(Donor donor) {
        DonorDTO dto = new DonorDTO();
        dto.setId(donor.getId());
        dto.setName(donor.getName());
        dto.setContactInfo(donor.getContactInfo());
        dto.setCreatedAt(donor.getCreatedAt());
        return dto;
    }
    
    /**
     * Convert DTO to entity
     * @param donorDTO donor DTO
     * @return donor entity
     */
    private Donor convertToEntity(DonorDTO donorDTO) {
        Donor donor = new Donor();
        donor.setId(donorDTO.getId());
        donor.setName(donorDTO.getName());
        donor.setContactInfo(donorDTO.getContactInfo());
        return donor;
    }
}
