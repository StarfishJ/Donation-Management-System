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
 * 分发记录控制器
 * 处理分发记录相关的HTTP请求
 */
@RestController
@RequestMapping("/api/distributions")
@CrossOrigin(origins = "http://localhost:3000") // 允许前端访问
public class DistributionController {
    
    @Autowired
    private DistributionService distributionService;
    
    /**
     * 获取所有分发记录
     * @return 分发记录列表
     */
    @GetMapping
    public ResponseEntity<List<DistributionDTO>> getAllDistributions() {
        List<DistributionDTO> distributions = distributionService.getAllDistributions();
        return ResponseEntity.ok(distributions);
    }
    
    /**
     * 根据ID获取分发记录
     * @param id 分发记录ID
     * @return 分发记录信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<DistributionDTO> getDistributionById(@PathVariable Long id) {
        Optional<DistributionDTO> distribution = distributionService.getDistributionById(id);
        return distribution.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 根据捐赠记录ID获取分发记录
     * @param donationId 捐赠记录ID
     * @return 该捐赠记录的所有分发记录
     */
    @GetMapping("/donation/{donationId}")
    public ResponseEntity<List<DistributionDTO>> getDistributionsByDonationId(@PathVariable Long donationId) {
        List<DistributionDTO> distributions = distributionService.getDistributionsByDonationId(donationId);
        return ResponseEntity.ok(distributions);
    }
    
    /**
     * 创建新分发记录
     * @param distributionDTO 分发记录信息
     * @return 创建的分发记录信息
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
     * 更新分发记录
     * @param id 分发记录ID
     * @param distributionDTO 更新的分发记录信息
     * @return 更新后的分发记录信息
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
     * 删除分发记录
     * @param id 分发记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistribution(@PathVariable Long id) {
        boolean deleted = distributionService.deleteDistribution(id);
        return deleted ? ResponseEntity.noContent().build() 
                      : ResponseEntity.notFound().build();
    }
    
    /**
     * 获取指定捐赠记录的总分发数量
     * @param donationId 捐赠记录ID
     * @return 总分发数量
     */
    @GetMapping("/donation/{donationId}/total")
    public ResponseEntity<Double> getTotalDistributedQuantity(@PathVariable Long donationId) {
        Double totalQuantity = distributionService.getTotalDistributedQuantity(donationId);
        return ResponseEntity.ok(totalQuantity);
    }
    
    /**
     * 获取指定捐赠记录的剩余数量
     * @param donationId 捐赠记录ID
     * @return 剩余数量
     */
    @GetMapping("/donation/{donationId}/remaining")
    public ResponseEntity<Double> getRemainingQuantity(@PathVariable Long donationId) {
        Double remainingQuantity = distributionService.getRemainingQuantity(donationId);
        return ResponseEntity.ok(remainingQuantity);
    }
    
    /**
     * 获取所有分发记录的汇总统计
     * @return 分发统计信息
     */
    @GetMapping("/summary")
    public ResponseEntity<Object[]> getDistributionSummary() {
        Object[] summary = distributionService.getDistributionSummary();
        return ResponseEntity.ok(summary);
    }
}
