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
 * 捐赠记录控制器
 * 处理捐赠记录相关的HTTP请求
 */
@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "http://localhost:3000") // 允许前端访问
public class DonationController {
    
    @Autowired
    private DonationService donationService;
    
    /**
     * 获取所有捐赠记录
     * @return 捐赠记录列表
     */
    @GetMapping
    public ResponseEntity<List<DonationDTO>> getAllDonations() {
        List<DonationDTO> donations = donationService.getAllDonations();
        return ResponseEntity.ok(donations);
    }
    
    /**
     * 根据ID获取捐赠记录
     * @param id 捐赠记录ID
     * @return 捐赠记录信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<DonationDTO> getDonationById(@PathVariable Long id) {
        Optional<DonationDTO> donation = donationService.getDonationById(id);
        return donation.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 根据捐赠者ID获取捐赠记录
     * @param donorId 捐赠者ID
     * @return 该捐赠者的所有捐赠记录
     */
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<DonationDTO>> getDonationsByDonorId(@PathVariable Long donorId) {
        List<DonationDTO> donations = donationService.getDonationsByDonorId(donorId);
        return ResponseEntity.ok(donations);
    }
    
    /**
     * 根据捐赠类型获取捐赠记录
     * @param type 捐赠类型
     * @return 该类型的所有捐赠记录
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<DonationDTO>> getDonationsByType(@PathVariable String type) {
        List<DonationDTO> donations = donationService.getDonationsByType(type);
        return ResponseEntity.ok(donations);
    }
    
    /**
     * 创建新捐赠记录
     * @param donationDTO 捐赠记录信息
     * @return 创建的捐赠记录信息
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
     * 更新捐赠记录
     * @param id 捐赠记录ID
     * @param donationDTO 更新的捐赠记录信息
     * @return 更新后的捐赠记录信息
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
     * 删除捐赠记录
     * @param id 捐赠记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        boolean deleted = donationService.deleteDonation(id);
        return deleted ? ResponseEntity.noContent().build() 
                      : ResponseEntity.notFound().build();
    }
    
    /**
     * 获取指定捐赠类型的总数量
     * @param type 捐赠类型
     * @return 该类型的总数量
     */
    @GetMapping("/type/{type}/total")
    public ResponseEntity<Double> getTotalQuantityByType(@PathVariable String type) {
        Double totalQuantity = donationService.getTotalQuantityByType(type);
        return ResponseEntity.ok(totalQuantity);
    }
    
    /**
     * 获取所有捐赠类型的库存汇总
     * @return 按类型分组的库存统计
     */
    @GetMapping("/inventory/summary")
    public ResponseEntity<List<Object[]>> getInventorySummary() {
        List<Object[]> summary = donationService.getInventorySummary();
        return ResponseEntity.ok(summary);
    }
}
