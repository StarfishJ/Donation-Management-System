/**
 * TypeScript Type Definitions
 * Defines all data types used in the system
 */

// Donor type
export interface Donor {
  id?: number;
  name: string;
  contactInfo?: string;
  createdAt?: string;
}

// Donation type
export interface Donation {
  id?: number;
  donorId: number;
  donationType: string;
  quantity: number;
  unit: string;
  description?: string;
  donationDate?: string;
  notes?: string;
  donorName?: string;
}

// Distribution type
export interface Distribution {
  id?: number;
  donationId: number;
  quantityDistributed: number;
  recipientInfo?: string;
  distributionDate?: string;
  notes?: string;
  donationType?: string;
  donorName?: string;
}

// Inventory report type
export interface InventoryReport {
  donationType: string;
  totalReceived: number;
  totalDistributed: number;
  remainingQuantity: number;
  unit: string;
}

// Donor report type
export interface DonorReport {
  donorId: number;
  donorName: string;
  contactInfo?: string;
  firstDonationDate: string;
  lastDonationDate: string;
  totalDonations: number;
  totalValue: number;
}

// System statistics type
export interface SystemStatistics {
  totalDonors: number;
  totalDonations: number;
  totalDistributions: number;
  totalDonationValue: number;
  totalDistributedValue: number;
  donationTypeStats?: Array<{
    type: string;
    count: number;
    totalAmount?: number;
  }>;
  donorStats?: {
    activeCount: number;
    newCount: number;
  };
  monthlyTrends?: Array<{
    month: string;
    type: string; // e.g. 'Donations' | 'Distributions'
    value: number;
  }>;
}

// API response type
export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

// Form type
export interface DonorFormData {
  name: string;
  phone: string;
  email?: string;
  address: string;
}

export interface DonationFormData {
  donorId: number;
  donationType: string;
  quantity: number;
  unit: string;
  description?: string;
  donationDate?: string;
  notes?: string;
}

export interface DistributionFormData {
  donationId: number;
  quantityDistributed: number;
  recipientName: string;
  recipientContact: string;
  distributionDate?: string;
  notes?: string;
}

// Donation type options
export const DONATION_TYPES = [
  { value: 'money', label: 'Cash' },
  { value: 'food', label: 'Food' },
  { value: 'clothing', label: 'Clothing' },
  { value: 'medicine', label: 'Medicine' },
  { value: 'furniture', label: 'Furniture' },
  { value: 'books', label: 'Books' },
  { value: 'toys', label: 'Toys' },
  { value: 'other', label: 'Other' }
] as const;

// Unit options
export const UNITS = [
  { value: 'pieces', label: 'Pieces' },
  { value: 'dollars', label: 'Dollars' },
  { value: 'kg', label: 'Kilograms' },
  { value: 'liter', label: 'Liters' },
  { value: 'box', label: 'Boxes' },
  { value: 'bag', label: 'Bags' }
] as const;
