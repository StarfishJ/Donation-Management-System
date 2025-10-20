package com.donation.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.donation.dto.DonorReportDTO;
import com.donation.dto.InventoryReportDTO;
import com.donation.repository.DistributionRepository;
import com.donation.repository.DonationRepository;
import com.donation.repository.DonorRepository;

/**
 * Report Service Layer
 * Handles business logic for various reports
 */
@Service
@Transactional(readOnly = true)
public class ReportService {
    
    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private DistributionRepository distributionRepository;
    
    @Autowired
    private DonorRepository donorRepository;
    
    /**
     * Generate inventory report
     * @return inventory report list
     */
    public List<InventoryReportDTO> generateInventoryReport() {
        List<InventoryReportDTO> inventoryReports = new ArrayList<>();
        
        // Get summary of all donation types
        List<Object[]> donationSummaries = donationRepository.getInventorySummary();
        
        for (Object[] summary : donationSummaries) {
            String donationType = (String) summary[0];
            Double totalReceived = ((Number) summary[1]).doubleValue();
            String unit = (String) summary[2];
            
            // Calculate total distributed quantity
            Double totalDistributed = 0.0;
            List<Object[]> distributions = distributionRepository.findByDonationId(
                donationRepository.findByDonationType(donationType).get(0).getId()
            ).stream()
            .map(d -> new Object[]{d.getQuantityDistributed()})
            .toList();
            
            for (Object[] dist : distributions) {
                totalDistributed += ((Number) dist[0]).doubleValue();
            }
            
            // Calculate remaining quantity
            Double remainingQuantity = totalReceived - totalDistributed;
            
            InventoryReportDTO report = new InventoryReportDTO(
                donationType, 
                totalReceived, 
                totalDistributed, 
                remainingQuantity, 
                unit
            );
            inventoryReports.add(report);
        }
        
        return inventoryReports;
    }
    
    /**
     * Generate donor report
     * @return donor report list
     */
    public List<DonorReportDTO> generateDonorReport() {
        List<DonorReportDTO> donorReports = new ArrayList<>();
        
        // Get all donors
        List<Object[]> donorData = donorRepository.findAll().stream()
            .map(donor -> {
                Long donorId = donor.getId();
                String donorName = donor.getName();
                String contactInfo = donor.getContactInfo();
                
                // Get donation records of the donor
                List<Object[]> donations = donationRepository.findByDonorId(donorId).stream()
                    .map(donation -> new Object[]{
                        donation.getDonationDate(),
                        donation.getQuantity(),
                        donation.getDonationType()
                    })
                    .toList();
                
                if (!donations.isEmpty()) {
                    // Calculate first and last donation dates
                    Object[] firstDonation = donations.stream()
                        .min((a, b) -> ((java.time.LocalDateTime) a[0]).compareTo((java.time.LocalDateTime) b[0]))
                        .orElse(null);
                    Object[] lastDonation = donations.stream()
                        .max((a, b) -> ((java.time.LocalDateTime) a[0]).compareTo((java.time.LocalDateTime) b[0]))
                        .orElse(null);
                    
                    // Calculate total donation count and total value
                    int totalDonations = donations.size();
                    double totalValue = donations.stream()
                        .mapToDouble(d -> ((Number) d[1]).doubleValue())
                        .sum();
                    
                    return new Object[]{
                        donorId, donorName, contactInfo,
                        firstDonation != null ? firstDonation[0] : null,
                        lastDonation != null ? lastDonation[0] : null,
                        totalDonations, totalValue
                    };
                }
                return null;
            })
            .filter(data -> data != null)
            .toList();
        
        for (Object[] data : donorData) {
            DonorReportDTO report = new DonorReportDTO(
                (Long) data[0],
                (String) data[1],
                (String) data[2],
                (java.time.LocalDateTime) data[3],
                (java.time.LocalDateTime) data[4],
                (Integer) data[5],
                ((Number) data[6]).doubleValue()
            );
            donorReports.add(report);
        }
        
        return donorReports;
    }
    
    /**
     * Get system statistics (default window: 30 days)
     */
    public Object getSystemStatistics() { return getSystemStatistics(30); }

    /**
     * Get system statistics with configurable recent days window
     */
    public Object getSystemStatistics(int recentDays) {
        final int window = Math.max(recentDays, 0);
        // Precompute lookup maps
        final java.util.List<com.donation.entity.Donation> allDonations = donationRepository.findAll();
        final java.util.Map<Long, String> donationIdToType = allDonations.stream()
            .collect(java.util.stream.Collectors.toMap(d -> d.getId(), d -> d.getDonationType()));
        final java.util.Map<String, Double> typeToDistributed = distributionRepository.findAll().stream()
            .collect(java.util.stream.Collectors.groupingBy(
                dist -> donationIdToType.getOrDefault(dist.getDonationId(), "unknown"),
                java.util.stream.Collectors.summingDouble(dist -> dist.getQuantityDistributed())
            ));
        return new Object() {
            public final Long totalDonors = donorRepository.count();
            public final Long totalDonations = donationRepository.count();
            public final Long totalDistributions = distributionRepository.count();
            public final Double totalDonationValue = donationRepository.findAll().stream()
                .mapToDouble(donation -> donation.getQuantity())
                .sum();
            public final Double totalDistributedValue = distributionRepository.findAll().stream()
                .mapToDouble(distribution -> distribution.getQuantityDistributed())
                .sum();
            
            // Chart data
            public final List<Object> donationTypeStats = donationRepository.findAll().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    donation -> donation.getDonationType(),
                    java.util.stream.Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> new Object() {
                    public final String type = entry.getKey();
                    public final Long count = entry.getValue();
                    public final Double totalAmount = donationRepository.findAll().stream()
                        .filter(d -> type.equals(d.getDonationType()))
                        .mapToDouble(d -> d.getQuantity())
                        .sum();
                    public final Double distributedAmount = typeToDistributed.getOrDefault(type, 0.0);
                    // percentAllocated is a fraction between 0 and 1
                    public final Double percentAllocated = (totalAmount != null && totalAmount > 0)
                        ? (distributedAmount / totalAmount)
                        : 0.0;
                    // percentPct is percent number in range 0..100 for direct display
                    public final Double percentPct = percentAllocated * 100.0;
                })
                .collect(java.util.stream.Collectors.toList());
            
            public final Object donorStats = new Object() {
                public final Long activeCount = donorRepository.count();
                public final Long newCount = donorRepository.countByCreatedAtAfter(LocalDateTime.now().minusDays(window));
            };
            
            // 暂时不提供月度趋势数据，等有真实数据时再添加
            public final List<Object> monthlyTrends = new ArrayList<>();
        };
    }
}
