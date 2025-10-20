package com.donation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.donation.dto.DonorReportDTO;
import com.donation.dto.InventoryReportDTO;
import com.donation.service.ReportService;

/**
 * 报告控制器
 * 处理报告相关的HTTP请求
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000") // 允许前端访问
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    /**
     * 生成库存报告
     * @return 库存报告列表
     */
    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryReportDTO>> generateInventoryReport() {
        List<InventoryReportDTO> inventoryReport = reportService.generateInventoryReport();
        return ResponseEntity.ok(inventoryReport);
    }
    
    /**
     * 生成捐赠者报告
     * @return 捐赠者报告列表
     */
    @GetMapping("/donors")
    public ResponseEntity<List<DonorReportDTO>> generateDonorReport() {
        List<DonorReportDTO> donorReport = reportService.generateDonorReport();
        return ResponseEntity.ok(donorReport);
    }
    
    /**
     * 获取系统统计信息
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Object> getSystemStatistics(@RequestParam(name = "days", required = false) Integer days) {
        Object statistics = (days == null) ? reportService.getSystemStatistics() : reportService.getSystemStatistics(days);
        return ResponseEntity.ok(statistics);
    }
}
