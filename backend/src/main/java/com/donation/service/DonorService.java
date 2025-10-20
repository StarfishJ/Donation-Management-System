package com.donation.service;

import com.donation.dto.DonorDTO;
import com.donation.entity.Donor;
import com.donation.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 捐赠者服务层
 * 处理捐赠者相关的业务逻辑
 */
@Service
@Transactional
public class DonorService {
    
    @Autowired
    private DonorRepository donorRepository;
    
    /**
     * 获取所有捐赠者
     * @return 捐赠者列表
     */
    @Transactional(readOnly = true)
    public List<DonorDTO> getAllDonors() {
        return donorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取捐赠者
     * @param id 捐赠者ID
     * @return 捐赠者信息
     */
    @Transactional(readOnly = true)
    public Optional<DonorDTO> getDonorById(Long id) {
        return donorRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    /**
     * 根据姓名查找捐赠者
     * @param name 捐赠者姓名
     * @return 匹配的捐赠者列表
     */
    @Transactional(readOnly = true)
    public List<DonorDTO> searchDonorsByName(String name) {
        return donorRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 创建新捐赠者
     * @param donorDTO 捐赠者信息
     * @return 创建的捐赠者信息
     */
    public DonorDTO createDonor(DonorDTO donorDTO) {
        Donor donor = convertToEntity(donorDTO);
        Donor savedDonor = donorRepository.save(donor);
        return convertToDTO(savedDonor);
    }
    
    /**
     * 更新捐赠者信息
     * @param id 捐赠者ID
     * @param donorDTO 更新的捐赠者信息
     * @return 更新后的捐赠者信息
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
     * 删除捐赠者
     * @param id 捐赠者ID
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
     * 获取捐赠者总数
     * @return 捐赠者总数
     */
    @Transactional(readOnly = true)
    public Long getTotalDonorsCount() {
        return donorRepository.countTotalDonors();
    }
    
    /**
     * 将实体转换为DTO
     * @param donor 捐赠者实体
     * @return 捐赠者DTO
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
     * 将DTO转换为实体
     * @param donorDTO 捐赠者DTO
     * @return 捐赠者实体
     */
    private Donor convertToEntity(DonorDTO donorDTO) {
        Donor donor = new Donor();
        donor.setId(donorDTO.getId());
        donor.setName(donorDTO.getName());
        donor.setContactInfo(donorDTO.getContactInfo());
        return donor;
    }
}
