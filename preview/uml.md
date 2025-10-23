# UML Diagrams for Donation Management System

## Class Diagram
```mermaid
classDiagram
  class Donor {
    +Long id
    +String name
    +String contactInfo
    +LocalDateTime createdAt
  }

  class Donation {
    +Long id
    +Long donorId
    +String donationType
    +Double quantity
    +String unit
    +String description
    +LocalDateTime donationDate
  }

  class Distribution {
    +Long id
    +Long donationId
    +Double quantityDistributed
    +String recipientName
    +String recipientContact
    +String recipientInfo
    +LocalDateTime distributionDate
    +String notes
  }

  Donor "1" -- "*" Donation : donates
  Donation "1" -- "*" Distribution : distributes
```

## Component Diagram
```mermaid
flowchart LR
  subgraph Frontend [Frontend (React + TypeScript + Ant Design)]
    UI[Pages & Components]
    APIClient[Axios API Service]
  end

  subgraph Backend [Backend (Spring Boot 3.2)]
    Controller[REST Controllers]
    Service[Service Layer]
    Repository[Repository (JPA)]
    Entity[Entities & DTOs]
  end

  DB[(PostgreSQL)]

  UI --> APIClient
  APIClient -- REST /api --> Controller
  Controller --> Service
  Service --> Repository
  Repository --> DB
```

## Sequence Diagram - Create Donation Flow
```mermaid
sequenceDiagram
  autonumber
  participant U as User
  participant FE as Frontend (React)
  participant API as Backend API (Controller)
  participant S as Service Layer
  participant R as Repository
  participant DB as PostgreSQL

  U->>FE: Submit donation form
  FE->>API: POST /api/donations (DonationDTO)
  API->>S: validate & createDonation(donationDTO)
  S->>R: save(Donation)
  R->>DB: INSERT donations
  DB-->>R: persisted entity
  R-->>S: Donation entity
  S-->>API: DonationDTO
  API-->>FE: 201 Created + DonationDTO
  FE-->>U: Show success message
```

## Sequence Diagram - Distribution with Inventory Check
```mermaid
sequenceDiagram
  autonumber
  participant U as User
  participant FE as Frontend (React)
  participant API as Backend API (Controller)
  participant S as Service Layer
  participant R as Repository
  participant DB as PostgreSQL

  U->>FE: Submit distribution form
  FE->>API: POST /api/distributions (DistributionDTO)
  API->>S: createDistribution(dto)
  S->>R: findDonationById(dto.donationId)
  R-->>S: Donation with total quantity and distributions
  S->>S: calculate remaining = total - sum(distributed)
  S->>S: validate remaining >= dto.quantityDistributed
  alt valid
    S->>R: save(Distribution)
    R->>DB: INSERT distributions
    DB-->>R: persisted entity
    R-->>S: Distribution entity
    S-->>API: DistributionDTO
    API-->>FE: 201 Created
    FE-->>U: Show success
  else insufficient
    S-->>API: throw validation error
    API-->>FE: 400 Bad Request
    FE-->>U: Show error: exceeds remaining
  end
```
