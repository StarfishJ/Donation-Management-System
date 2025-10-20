/**
 * API Service
 * Handle all HTTP requests to backend
 */

import axios, { AxiosResponse } from 'axios';
import { Donor, Donation, Distribution, InventoryReport, DonorReport, SystemStatistics } from '../types';

// Create axios instance
const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    console.log('Sending request:', config.method?.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response: AxiosResponse) => {
    console.log('Received response:', response.status, response.config.url);
    return response;
  },
  (error) => {
    console.error('Response error:', error.response?.status, error.response?.data);
    return Promise.reject(error);
  }
);

// Donor API
export const donorApi = {
  // Get all donors
  getAll: (): Promise<Donor[]> => 
    api.get('/donors').then(res => res.data),

  // Get donor by ID
  getById: (id: number): Promise<Donor> => 
    api.get(`/donors/${id}`).then(res => res.data),

  // Search donors
  search: (name: string): Promise<Donor[]> => 
    api.get('/donors/search', { params: { name } }).then(res => res.data),

  // Create donor
  create: (donor: Partial<Donor>): Promise<Donor> => 
    api.post('/donors', donor).then(res => res.data),

  // Update donor
  update: (id: number, donor: Partial<Donor>): Promise<Donor> => 
    api.put(`/donors/${id}`, donor).then(res => res.data),

  // Delete donor
  delete: (id: number): Promise<void> => 
    api.delete(`/donors/${id}`).then(res => res.data),

  // Get total donor count
  getCount: (): Promise<number> => 
    api.get('/donors/count').then(res => res.data),
};

// Donation API
export const donationApi = {
  // Get all donations
  getAll: (): Promise<Donation[]> => 
    api.get('/donations').then(res => res.data),

  // Get donation by ID
  getById: (id: number): Promise<Donation> => 
    api.get(`/donations/${id}`).then(res => res.data),

  // Get donations by donor ID
  getByDonorId: (donorId: number): Promise<Donation[]> => 
    api.get(`/donations/donor/${donorId}`).then(res => res.data),

  // Get donations by type
  getByType: (type: string): Promise<Donation[]> => 
    api.get(`/donations/type/${type}`).then(res => res.data),

  // Create donation
  create: (donation: Partial<Donation>): Promise<Donation> => 
    api.post('/donations', donation).then(res => res.data),

  // Update donation
  update: (id: number, donation: Partial<Donation>): Promise<Donation> => 
    api.put(`/donations/${id}`, donation).then(res => res.data),

  // Delete donation
  delete: (id: number): Promise<void> => 
    api.delete(`/donations/${id}`).then(res => res.data),

  // Get total by type
  getTotalByType: (type: string): Promise<number> => 
    api.get(`/donations/type/${type}/total`).then(res => res.data),

  // Get inventory summary
  getInventorySummary: (): Promise<any[]> => 
    api.get('/donations/inventory/summary').then(res => res.data),
};

// Distribution API
export const distributionApi = {
  // Get all distributions
  getAll: (): Promise<Distribution[]> => 
    api.get('/distributions').then(res => res.data),

  // Get distribution by ID
  getById: (id: number): Promise<Distribution> => 
    api.get(`/distributions/${id}`).then(res => res.data),

  // Get distributions by donation ID
  getByDonationId: (donationId: number): Promise<Distribution[]> => 
    api.get(`/distributions/donation/${donationId}`).then(res => res.data),

  // Create distribution
  create: (distribution: Partial<Distribution>): Promise<Distribution> => 
    api.post('/distributions', distribution).then(res => res.data),

  // Update distribution
  update: (id: number, distribution: Partial<Distribution>): Promise<Distribution> => 
    api.put(`/distributions/${id}`, distribution).then(res => res.data),

  // Delete distribution
  delete: (id: number): Promise<void> => 
    api.delete(`/distributions/${id}`).then(res => res.data),

  // Get total distributed
  getTotalDistributed: (donationId: number): Promise<number> => 
    api.get(`/distributions/donation/${donationId}/total`).then(res => res.data),

  // Get remaining quantity
  getRemaining: (donationId: number): Promise<number> => 
    api.get(`/distributions/donation/${donationId}/remaining`).then(res => res.data),

  // Get distribution summary
  getSummary: (): Promise<any[]> => 
    api.get('/distributions/summary').then(res => res.data),
};

// Report API
export const reportApi = {
  // Generate inventory report
  getInventoryReport: (): Promise<InventoryReport[]> => 
    api.get('/reports/inventory').then(res => res.data),

  // Generate donor report
  getDonorReport: (): Promise<DonorReport[]> => 
    api.get('/reports/donors').then(res => res.data),

  // Get system statistics, optional days window for new donors
  getStatistics: (days?: number): Promise<SystemStatistics> => 
    api.get('/reports/statistics', { params: days ? { days } : undefined }).then(res => res.data),
};

export default api;
