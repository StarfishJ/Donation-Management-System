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
 * Report Controller
 * Handles report related HTTP requests
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000") // allow frontend access
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    /**
    * Generate inventory report
     * @return inventory report list
     */
    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryReportDTO>> generateInventoryReport() {
        List<InventoryReportDTO> inventoryReport = reportService.generateInventoryReport();
        return ResponseEntity.ok(inventoryReport);
    }
    
    /**
    * Generate donor report
     * @return donor report list
     */
    @GetMapping("/donors")
    public ResponseEntity<List<DonorReportDTO>> generateDonorReport() {
        List<DonorReportDTO> donorReport = reportService.generateDonorReport();
        return ResponseEntity.ok(donorReport);
    }
    
    /**
    * Get system statistics
     * @return statistics information
     */
    @GetMapping("/statistics")
    public ResponseEntity<Object> getSystemStatistics(@RequestParam(name = "days", required = false) Integer days) {
        Object statistics = (days == null) ? reportService.getSystemStatistics() : reportService.getSystemStatistics(days);
        return ResponseEntity.ok(statistics);
    }
}
