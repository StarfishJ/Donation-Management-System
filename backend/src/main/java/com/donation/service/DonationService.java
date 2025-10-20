package com.donation.service;

import com.donation.dto.DonationDTO;
import com.donation.entity.Donation;
import com.donation.entity.Donor;
import com.donation.repository.DonationRepository;
import com.donation.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 捐赠服务层
 * 处理捐赠记录相关的业务逻辑
 */
@Service
@Transactional
public class DonationService {
    
    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private DonorRepository donorRepository;
    
    /**
     * 获取所有捐赠记录
     * @return 捐赠记录列表
     */
    @Transactional(readOnly = true)
    public List<DonationDTO> getAllDonations() {
        return donationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取捐赠记录
     * @param id 捐赠记录ID
     * @return 捐赠记录信息
     */
    @Transactional(readOnly = true)
    public Optional<DonationDTO> getDonationById(Long id) {
        return donationRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    /**
     * 根据捐赠者ID获取捐赠记录
     * @param donorId 捐赠者ID
     * @return 该捐赠者的所有捐赠记录
     */
    @Transactional(readOnly = true)
    public List<DonationDTO> getDonationsByDonorId(Long donorId) {
        return donationRepository.findByDonorId(donorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据捐赠类型获取捐赠记录
     * @param donationType 捐赠类型
     * @return 该类型的所有捐赠记录
     */
    @Transactional(readOnly = true)
    public List<DonationDTO> getDonationsByType(String donationType) {
        return donationRepository.findByDonationType(donationType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 创建新捐赠记录
     * @param donationDTO 捐赠记录信息
     * @return 创建的捐赠记录信息
     */
    public DonationDTO createDonation(DonationDTO donationDTO) {
        // 验证捐赠者是否存在
        Donor donor = donorRepository.findById(donationDTO.getDonorId())
                .orElseThrow(() -> new RuntimeException("捐赠者不存在: " + donationDTO.getDonorId()));
        
        Donation donation = convertToEntity(donationDTO);
        Donation savedDonation = donationRepository.save(donation);
        return convertToDTO(savedDonation);
    }
    
    /**
     * 更新捐赠记录
     * @param id 捐赠记录ID
     * @param donationDTO 更新的捐赠记录信息
     * @return 更新后的捐赠记录信息
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
     * 删除捐赠记录
     * @param id 捐赠记录ID
     * @return 是否删除成功
     */
    public boolean deleteDonation(Long id) {
        if (donationRepository.existsById(id)) {
            donationRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * 获取指定捐赠类型的总数量
     * @param donationType 捐赠类型
     * @return 该类型的总数量
     */
    @Transactional(readOnly = true)
    public Double getTotalQuantityByType(String donationType) {
        return donationRepository.getTotalQuantityByType(donationType);
    }
    
    /**
     * 获取所有捐赠类型的库存汇总
     * @return 按类型分组的库存统计
     */
    @Transactional(readOnly = true)
    public List<Object[]> getInventorySummary() {
        return donationRepository.getInventorySummary();
    }
    
    /**
     * 将实体转换为DTO
     * @param donation 捐赠记录实体
     * @return 捐赠记录DTO
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
        
        // 设置捐赠者姓名
        if (donation.getDonor() != null) {
            dto.setDonorName(donation.getDonor().getName());
        }
        
        return dto;
    }
    
    /**
     * 将DTO转换为实体
     * @param donationDTO 捐赠记录DTO
     * @return 捐赠记录实体
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
