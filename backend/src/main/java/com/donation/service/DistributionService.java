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
     * 获取所有分发记录
     * @return 分发记录列表
     */
    @Transactional(readOnly = true)
    public List<DistributionDTO> getAllDistributions() {
        return distributionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取分发记录
     * @param id 分发记录ID
     * @return 分发记录信息
     */
    @Transactional(readOnly = true)
    public Optional<DistributionDTO> getDistributionById(Long id) {
        return distributionRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    /**
     * 根据捐赠记录ID获取分发记录
     * @param donationId 捐赠记录ID
     * @return 该捐赠记录的所有分发记录
     */
    @Transactional(readOnly = true)
    public List<DistributionDTO> getDistributionsByDonationId(Long donationId) {
        return distributionRepository.findByDonationId(donationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 创建新分发记录
     * @param distributionDTO 分发记录信息
     * @return 创建的分发记录信息
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
     * 更新分发记录
     * @param id 分发记录ID
     * @param distributionDTO 更新的分发记录信息
     * @return 更新后的分发记录信息
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
     * 删除分发记录
     * @param id 分发记录ID
     * @return 是否删除成功
     */
    public boolean deleteDistribution(Long id) {
        if (distributionRepository.existsById(id)) {
            distributionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * 获取指定捐赠记录的总分发数量
     * @param donationId 捐赠记录ID
     * @return 总分发数量
     */
    @Transactional(readOnly = true)
    public Double getTotalDistributedQuantity(Long donationId) {
        return distributionRepository.getTotalDistributedQuantity(donationId);
    }
    
    /**
     * 获取指定捐赠记录的剩余数量
     * @param donationId 捐赠记录ID
     * @return 剩余数量
     */
    @Transactional(readOnly = true)
    public Double getRemainingQuantity(Long donationId) {
        return distributionRepository.getRemainingQuantity(donationId);
    }
    
    /**
     * 获取所有分发记录的汇总统计
     * @return 分发统计信息
     */
    @Transactional(readOnly = true)
    public Object[] getDistributionSummary() {
        return distributionRepository.getDistributionSummary();
    }
    
    /**
     * 将实体转换为DTO
     * @param distribution 分发记录实体
     * @return 分发记录DTO
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
        
        // 设置关联信息
        if (distribution.getDonation() != null) {
            dto.setDonationType(distribution.getDonation().getDonationType());
            if (distribution.getDonation().getDonor() != null) {
                dto.setDonorName(distribution.getDonation().getDonor().getName());
            }
        }
        
        return dto;
    }
    
    /**
     * 将DTO转换为实体
     * @param distributionDTO 分发记录DTO
     * @return 分发记录实体
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
