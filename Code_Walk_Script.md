# Code Walk Script - Donation Management System

## 🎯 **Introduction (2 minutes)**

Good morning/afternoon everyone! Today I'll be walking you through the **Donation Management System**, a full-stack web application designed to help shelters manage their donation processes efficiently.

### **Project Overview**
This system addresses the real-world problem of manual donation tracking in shelters, providing a digital solution for:
- **Donor Management**: Centralized donor information
- **Donation Registration**: Multi-type donation tracking
- **Distribution Management**: Real-time inventory control
- **Reporting & Analytics**: Data-driven insights

### **Technology Stack**
- **Frontend**: React 18 + TypeScript + Ant Design
- **Backend**: Spring Boot 3.2 + JPA + PostgreSQL
- **Architecture**: RESTful API with frontend-backend separation

---

## 🏗️ **Architecture Overview (3 minutes)**

### **System Architecture**
```
┌─────────────────┐
│   Frontend      │  React + TypeScript + Ant Design
│   (Port 3000)   │
└─────────────────┘
         │ HTTP/REST API
┌─────────────────┐
│   Backend       │  Spring Boot + JPA
│   (Port 8080)   │
└─────────────────┘
         │ JDBC
┌─────────────────┐
│   Database      │  PostgreSQL (Supabase)
│   (Port 5432)   │
└─────────────────┘
```

### **Backend Layered Architecture**
- **Controller Layer**: REST API endpoints
- **Service Layer**: Business logic and transaction management
- **Repository Layer**: Data access with Spring Data JPA
- **Entity Layer**: Domain models with JPA annotations

### **Frontend Component Architecture**
- **Pages**: Route-level components
- **Services**: API communication layer
- **Types**: TypeScript type definitions
- **Components**: Reusable UI components

---

## 🚀 **Application Startup (2 minutes)**

### **Backend Startup**
Let's start with the main application class:

```java
// backend/src/main/java/com/donation/DonationApplication.java
@SpringBootApplication
public class DonationApplication {
    public static void main(String[] args) {
        SpringApplication.run(DonationApplication.class, args);
        System.out.println("Donation Management System Backend Service Started!");
        System.out.println("Access URL: http://localhost:8080/api");
    }
}
```

**Key Points**:
- `@SpringBootApplication`: Combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`
- Auto-configuration sets up Spring Boot components
- Embedded Tomcat server starts on port 8080

### **Configuration**
```yaml
# backend/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:postgresql://db.kbbiipcsdaqqtapncdtf.supabase.co:5432/postgres?sslmode=require
    username: postgres
    password: MXoZCJErQcTKGTk8
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

**Configuration Highlights**:
- **Supabase PostgreSQL**: Cloud-hosted database
- **JPA Auto-update**: Schema automatically updated
- **SQL Logging**: Development debugging enabled

### **Frontend Startup**
```typescript
// frontend/src/index.tsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
```

**Frontend Dependencies**:
```json
// frontend/package.json
{
  "dependencies": {
    "react": "^18.2.0",
    "typescript": "^4.9.0",
    "antd": "^5.12.0",
    "axios": "^1.6.0",
    "react-router-dom": "^6.8.0",
    "@ant-design/charts": "^2.6.5"
  },
  "proxy": "http://localhost:8080"
}
```

---

## 📊 **Database Design (3 minutes)**

### **Entity Relationships**
```
Donors (1) ──────── (N) Donations (1) ──────── (N) Distributions
```

### **Core Entities**

**Donor Entity**:
```java
// backend/src/main/java/com/donation/entity/Donor.java
@Entity
@Table(name = "donors")
public class Donor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Donor name cannot be empty")
    @Size(max = 100, message = "Name length cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;
    
    @Size(max = 200, message = "Contact information length cannot exceed 200 characters")
    @Column(name = "contact_info", length = 200)
    private String contactInfo;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // Association: A donor can have multiple donation records
    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Donation> donations;
}
```

**Donation Entity**:
```java
// backend/src/main/java/com/donation/entity/Donation.java
@Entity
@Table(name = "donations")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Donor ID cannot be empty")
    @Column(name = "donor_id", nullable = false)
    private Long donorId;
    
    @NotBlank(message = "Donation type cannot be empty")
    @Size(max = 50, message = "Donation type length cannot exceed 50 characters")
    @Column(name = "donation_type", nullable = false, length = 50)
    private String donationType;
    
    @NotNull(message = "Donation quantity cannot be empty")
    @Positive(message = "Donation quantity must be greater than 0")
    @Column(nullable = false)
    private Double quantity;
    
    // Association: Many donations to one donor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", insertable = false, updatable = false)
    private Donor donor;
    
    // Association: One donation to many distributions
    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Distribution> distributions;
}
```

**Distribution Entity**:
```java
// backend/src/main/java/com/donation/entity/Distribution.java
@Entity
@Table(name = "distributions")
public class Distribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Donation record ID cannot be empty")
    @Column(name = "donation_id", nullable = false)
    private Long donationId;
    
    @NotNull(message = "Distribution quantity cannot be empty")
    @Positive(message = "Distribution quantity must be greater than 0")
    @Column(name = "quantity_distributed", nullable = false)
    private Double quantityDistributed;
    
    @NotNull(message = "Recipient name cannot be empty")
    @Size(max = 100, message = "Recipient name length cannot exceed 100 characters")
    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;
    
    // Association: Many distributions to one donation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id", insertable = false, updatable = false)
    private Donation donation;
}
```

**Key Design Decisions**:
- **Foreign Key Constraints**: Ensure referential integrity
- **Cascade Operations**: Handle related data deletion
- **Lazy Loading**: Optimize performance with `FetchType.LAZY`
- **Validation Annotations**: Data integrity at entity level

---

## 🔧 **Backend Implementation (5 minutes)**

### **Repository Layer**
```java
// backend/src/main/java/com/donation/repository/DonorRepository.java
public interface DonorRepository extends JpaRepository<Donor, Long> {
    List<Donor> findByNameContainingIgnoreCase(String name);
    Long countTotalDonors();
}
```

**Spring Data JPA Benefits**:
- **Method Name Queries**: Automatic query generation
- **Custom Queries**: `@Query` annotation support
- **Pagination**: Built-in pagination support
- **Auditing**: Automatic timestamp management

### **Service Layer**
```java
// backend/src/main/java/com/donation/service/DonorService.java
@Service
@Transactional
public class DonorService {
    @Autowired
    private DonorRepository donorRepository;
    
    @Transactional(readOnly = true)
    public List<DonorDTO> getAllDonors() {
        return donorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public DonorDTO createDonor(DonorDTO donorDTO) {
        Donor donor = convertToEntity(donorDTO);
        Donor savedDonor = donorRepository.save(donor);
        return convertToDTO(savedDonor);
    }
    
    private DonorDTO convertToDTO(Donor donor) {
        DonorDTO dto = new DonorDTO();
        dto.setId(donor.getId());
        dto.setName(donor.getName());
        dto.setContactInfo(donor.getContactInfo());
        dto.setCreatedAt(donor.getCreatedAt());
        return dto;
    }
}
```

**Service Layer Features**:
- **Transaction Management**: `@Transactional` annotation
- **DTO Pattern**: Entity-DTO conversion
- **Business Logic**: Validation and processing
- **Read-Only Transactions**: Optimized for queries

### **Controller Layer**
```java
// backend/src/main/java/com/donation/controller/DonorController.java
@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "http://localhost:3000")
public class DonorController {
    @Autowired
    private DonorService donorService;
    
    @GetMapping
    public ResponseEntity<List<DonorDTO>> getAllDonors() {
        List<DonorDTO> donors = donorService.getAllDonors();
        return ResponseEntity.ok(donors);
    }
    
    @PostMapping
    public ResponseEntity<DonorDTO> createDonor(@Valid @RequestBody DonorDTO donorDTO) {
        try {
            DonorDTO createdDonor = donorService.createDonor(donorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDonor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
```

**REST API Design**:
- **RESTful URLs**: Resource-based naming
- **HTTP Methods**: GET, POST, PUT, DELETE
- **Response Codes**: Proper status codes
- **CORS Support**: Frontend integration

### **Core Business Logic - Inventory Validation**
```java
// backend/src/main/java/com/donation/service/DistributionService.java
public DistributionDTO createDistribution(DistributionDTO distributionDTO) {
    // Validate donation exists
    Donation donation = donationRepository.findById(distributionDTO.getDonationId())
            .orElseThrow(() -> new RuntimeException("Donation record not found: " + distributionDTO.getDonationId()));
    
    // Check remaining quantity
    Double remainingQuantity = distributionRepository.getRemainingQuantity(distributionDTO.getDonationId());
    if (distributionDTO.getQuantityDistributed() > remainingQuantity) {
        throw new RuntimeException("Distributed quantity exceeds remaining. Remaining: " + remainingQuantity);
    }
    
    Distribution distribution = convertToEntity(distributionDTO);
    Distribution savedDistribution = distributionRepository.save(distribution);
    return convertToDTO(savedDistribution);
}
```

**Business Logic Highlights**:
- **Data Validation**: Multi-layer validation
- **Inventory Control**: Real-time quantity checking
- **Error Handling**: Clear error messages
- **Transaction Safety**: ACID compliance

---

## 🎨 **Frontend Implementation (5 minutes)**

### **Application Structure**
```typescript
// frontend/src/App.tsx
const App: React.FC = () => {
  return (
    <ConfigProvider locale={zhCN}>
      <Router>
        <AppContent />
      </Router>
    </ConfigProvider>
  );
};

const AppContent: React.FC = () => {
  const [collapsed, setCollapsed] = React.useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  
  const menuItems = [
    { key: '/', icon: <HomeOutlined />, label: 'Dashboard' },
    { key: '/donors', icon: <UserOutlined />, label: 'Donor Management' },
    { key: '/donations', icon: <GiftOutlined />, label: 'Donation Records' },
    { key: '/distributions', icon: <ShareAltOutlined />, label: 'Distribution Records' },
    { key: '/reports', icon: <BarChartOutlined />, label: 'Reports & Statistics' },
  ];
  
  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider collapsible collapsed={collapsed} onCollapse={setCollapsed}>
        <Menu
          theme="dark"
          selectedKeys={[location.pathname]}
          mode="inline"
          items={menuItems}
          onClick={({ key }) => navigate(key)}
        />
      </Sider>
      <Layout>
        <Header>
          <h2>Donation Management System</h2>
        </Header>
        <Content>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/donors" element={<DonorManagement />} />
            <Route path="/donations" element={<DonationManagement />} />
            <Route path="/distributions" element={<DistributionManagement />} />
            <Route path="/reports" element={<Reports />} />
          </Routes>
        </Content>
      </Layout>
    </Layout>
  );
};
```

### **TypeScript Type Definitions**
```typescript
// frontend/src/types/index.ts
export interface Donor {
  id?: number;
  name: string;
  contactInfo?: string;
  createdAt?: string;
}

export interface Donation {
  id?: number;
  donorId: number;
  donationType: string;
  quantity: number;
  unit: string;
  description?: string;
  donationDate?: string;
  donorName?: string;
}

export interface Distribution {
  id?: number;
  donationId: number;
  quantityDistributed: number;
  recipientName: string;
  recipientContact: string;
  recipientInfo?: string;
  distributionDate?: string;
  notes?: string;
}
```

### **API Service Layer**
```typescript
// frontend/src/services/api.ts
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

export const donorApi = {
  getAll: (): Promise<Donor[]> => 
    api.get('/donors').then(res => res.data),
  
  create: (donor: Partial<Donor>): Promise<Donor> => 
    api.post('/donors', donor).then(res => res.data),
  
  update: (id: number, donor: Partial<Donor>): Promise<Donor> => 
    api.put(`/donors/${id}`, donor).then(res => res.data),
  
  delete: (id: number): Promise<void> => 
    api.delete(`/donors/${id}`).then(res => res.data),
};
```

### **React Hooks Usage**
```typescript
// frontend/src/pages/Dashboard.tsx
const Dashboard: React.FC = () => {
  const [loading, setLoading] = useState(true);
  const [statistics, setStatistics] = useState<SystemStatistics | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [recentDays, setRecentDays] = useState<number>(30);
  
  useEffect(() => {
    loadStatistics(recentDays);
  }, [recentDays]);
  
  const loadStatistics = async (days?: number) => {
    try {
      setLoading(true);
      setError(null);
      const [stats, inv, donors] = await Promise.all([
        reportApi.getStatistics(days),
        reportApi.getInventoryReport(),
        reportApi.getDonorReport(),
      ]);
      setStatistics(stats);
      setInventoryReport(inv || []);
      setDonorReport(donors || []);
    } catch (err) {
      console.error('Failed to load statistics:', err);
      setError('Failed to load statistics, please try again later');
    } finally {
      setLoading(false);
    }
  };
};
```

**Frontend Features**:
- **TypeScript**: Compile-time type checking
- **React Hooks**: Modern state management
- **Ant Design**: Enterprise UI components
- **Axios**: HTTP client with interceptors
- **React Router**: Single-page application routing

---

## 📊 **Reporting System (3 minutes)**

### **Report Service Implementation**
```java
// backend/src/main/java/com/donation/service/ReportService.java
@Service
@Transactional(readOnly = true)
public class ReportService {
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
                donationType, totalReceived, totalDistributed, remainingQuantity, unit
            );
            inventoryReports.add(report);
        }
        
        return inventoryReports;
    }
}
```

### **Frontend Report Display**
```typescript
// frontend/src/pages/Reports.tsx
const Reports: React.FC = () => {
  const [inventoryReports, setInventoryReports] = useState<InventoryReport[]>([]);
  const [donorReports, setDonorReports] = useState<DonorReport[]>([]);
  const [statistics, setStatistics] = useState<SystemStatistics | null>(null);
  
  const loadReports = async () => {
    try {
      setLoading(true);
      setError(null);
      
      const [inventoryData, donorData, statsData] = await Promise.all([
        reportApi.getInventoryReport(),
        reportApi.getDonorReport(),
        reportApi.getStatistics()
      ]);
      
      setInventoryReports(inventoryData);
      setDonorReports(donorData);
      setStatistics(statsData);
    } catch (err) {
      console.error('Failed to load reports:', err);
      setError('Failed to load reports, please try again later');
    } finally {
      setLoading(false);
    }
  };
  
  const inventoryColumns = [
    { title: 'Donation Type', dataIndex: 'donationType', key: 'donationType' },
    { title: 'Total Received', dataIndex: 'totalReceived', key: 'totalReceived' },
    { title: 'Total Distributed', dataIndex: 'totalDistributed', key: 'totalDistributed' },
    { title: 'Remaining Quantity', dataIndex: 'remainingQuantity', key: 'remainingQuantity' },
    { title: 'Distribution Rate', key: 'distributionRate' },
  ];
};
```

---

## 🔄 **Data Flow Demonstration (3 minutes)**

### **Complete User Journey**

**1. User Creates a Donor**:
```
Frontend Form → API Call → Controller → Service → Repository → Database
```

**2. User Registers a Donation**:
```
Frontend Form → Validation → API Call → Controller → Service → Repository → Database
```

**3. User Records Distribution**:
```
Frontend Form → Inventory Check → API Call → Controller → Service → 
Business Logic Validation → Repository → Database
```

### **Real-time Inventory Tracking**
The system automatically calculates remaining quantities:
```java
// Calculate remaining quantity
Double remainingQuantity = totalReceived - totalDistributed;
if (distributionDTO.getQuantityDistributed() > remainingQuantity) {
    throw new RuntimeException("Distributed quantity exceeds remaining. Remaining: " + remainingQuantity);
}
```

---

## 🚀 **Key Technical Highlights (2 minutes)**

### **1. Type Safety**
- **Backend**: Java generics and validation annotations
- **Frontend**: TypeScript interfaces and type checking
- **API**: Consistent DTO patterns

### **2. Transaction Management**
- **ACID Compliance**: Database transactions
- **Spring Transactions**: `@Transactional` annotation
- **Read-Only Optimization**: Query performance

### **3. Error Handling**
- **Backend**: Exception handling with meaningful messages
- **Frontend**: User-friendly error displays
- **API**: Proper HTTP status codes

### **4. Performance Optimizations**
- **Lazy Loading**: JPA `FetchType.LAZY`
- **Connection Pooling**: Database connection management
- **Frontend Caching**: React state management

### **5. Security Considerations**
- **Input Validation**: Multi-layer validation
- **SQL Injection Prevention**: JPA parameterized queries
- **CORS Configuration**: Cross-origin request handling

---

## 🎯 **Future Enhancements (1 minute)**

### **Planned Improvements**
1. **Authentication & Authorization**: JWT-based user management
2. **Real-time Updates**: WebSocket integration
3. **Mobile App**: React Native implementation
4. **Advanced Analytics**: Machine learning insights
5. **API Documentation**: Swagger/OpenAPI integration

### **Scalability Considerations**
1. **Microservices**: Service decomposition
2. **Caching**: Redis integration
3. **Load Balancing**: Multiple server instances
4. **Database Optimization**: Read replicas and indexing

---

## ❓ **Q&A Session**

### **Common Questions**
- **Q**: How do you handle concurrent access to inventory?
- **A**: Database transactions with optimistic locking

- **Q**: What about data backup and recovery?
- **A**: Supabase provides automatic backups and point-in-time recovery

- **Q**: How would you scale this system?
- **A**: Microservices architecture with Redis caching and database sharding

---

## 🎯 **Conclusion**

This Donation Management System demonstrates:
- **Full-stack Development**: Modern web technologies
- **Clean Architecture**: Separation of concerns
- **Business Logic**: Real-world problem solving
- **Code Quality**: Best practices and patterns
- **Scalability**: Future-ready design

Thank you for your attention! I'm happy to answer any questions about the implementation details.

---

## 📋 **Quick Reference**

### **Key Files**
- **Backend Entry**: `backend/src/main/java/com/donation/DonationApplication.java`
- **Frontend Entry**: `frontend/src/index.tsx`
- **Main App**: `frontend/src/App.tsx`
- **API Service**: `frontend/src/services/api.ts`
- **Entities**: `backend/src/main/java/com/donation/entity/`
- **Controllers**: `backend/src/main/java/com/donation/controller/`
- **Services**: `backend/src/main/java/com/donation/service/`

### **Key Technologies**
- **Spring Boot 3.2**: Enterprise Java framework
- **React 18**: Modern UI library
- **TypeScript**: Type-safe JavaScript
- **PostgreSQL**: Relational database
- **Ant Design**: UI component library
- **Axios**: HTTP client
- **JPA**: Object-relational mapping
