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
 * 捐赠者控制器
 * 处理捐赠者相关的HTTP请求
 */
@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "http://localhost:3000") // 允许前端访问
public class DonorController {
    
    @Autowired
    private DonorService donorService;
    
    /**
     * 获取所有捐赠者
     * @return 捐赠者列表
     */
    @GetMapping
    public ResponseEntity<List<DonorDTO>> getAllDonors() {
        List<DonorDTO> donors = donorService.getAllDonors();
        return ResponseEntity.ok(donors);
    }
    
    /**
     * 根据ID获取捐赠者
     * @param id 捐赠者ID
     * @return 捐赠者信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<DonorDTO> getDonorById(@PathVariable Long id) {
        Optional<DonorDTO> donor = donorService.getDonorById(id);
        return donor.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 根据姓名搜索捐赠者
     * @param name 捐赠者姓名
     * @return 匹配的捐赠者列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<DonorDTO>> searchDonorsByName(@RequestParam String name) {
        List<DonorDTO> donors = donorService.searchDonorsByName(name);
        return ResponseEntity.ok(donors);
    }
    
    /**
     * 创建新捐赠者
     * @param donorDTO 捐赠者信息
     * @return 创建的捐赠者信息
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
     * 更新捐赠者信息
     * @param id 捐赠者ID
     * @param donorDTO 更新的捐赠者信息
     * @return 更新后的捐赠者信息
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
     * 删除捐赠者
     * @param id 捐赠者ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonor(@PathVariable Long id) {
        boolean deleted = donorService.deleteDonor(id);
        return deleted ? ResponseEntity.noContent().build() 
                      : ResponseEntity.notFound().build();
    }
    
    /**
     * 获取捐赠者总数
     * @return 捐赠者总数
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalDonorsCount() {
        Long count = donorService.getTotalDonorsCount();
        return ResponseEntity.ok(count);
    }
}
