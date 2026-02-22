# GraphQL ERP — Backend

Multi-tenant, modular ERP system built with **Spring Boot 4**, **Netflix DGS 11**, and **Java 25**.

## Architecture

```
┌──────────────────────────────────────────────────────────────┐
│  app                                                         │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  GraphQL Resolvers (DGS) + Data Loaders + Directives    │ │
│  └──────────────────────────┬──────────────────────────────┘ │
│                             │                                │
│  ┌──────────────────────────┴──────────────────────────────┐ │
│  │            Use Cases (Application Layer)                 │ │
│  │    core-application / sales-application / hr-app / ...   │ │
│  └──────────────────────────┬──────────────────────────────┘ │
│                             │                                │
│  ┌──────────────────────────┴──────────────────────────────┐ │
│  │              Domain Models (Pure Java)                   │ │
│  │    core-domain / sales-domain / hr-domain / ...          │ │
│  └──────────────────────────┬──────────────────────────────┘ │
│                             │                                │
│  ┌──────────────────────────┴──────────────────────────────┐ │
│  │         Platform (JPA, Flyway, Event Bus)               │ │
│  │    core-platform / sales-platform / hr-platform / ...    │ │
│  └─────────────────────────────────────────────────────────┘ │
│                                                              │
│  core-kernel (shared primitives: Identifier, ValueObject,    │
│               AggregateRoot, DomainEvent, TenantContext)     │
└──────────────────────────────────────────────────────────────┘
```

Every bounded-context module follows the **hexagonal architecture** pattern with three sub-modules:

| Layer | Package | Responsibility |
|---|---|---|
| **domain** | `module-domain` | Aggregate roots, entities, value objects, repository ports, domain events |
| **application** | `module-application` | Use case interfaces, command/query DTOs |
| **platform** | `module-platform` | JPA entities, repository adapters, event listeners, external service adapters |

The **app** module wires everything together: DGS GraphQL resolvers, data loaders, security filters, and Spring Boot configuration.

## Modules

| Module | Purpose | Key Aggregates |
|---|---|---|
| **Core** | Tenants, Auth, Companies, Departments, Employees, Business Partners, Reference Data | Tenant, Company, Department, Employee, BusinessPartner, Currency, Unit, PaymentTerm, Tax |
| **Sales** | Sales order lifecycle | SalesOrder (with lines) |
| **Inventory** | Warehouses, items, stock tracking | Warehouse, Category, Item, StockLevel, StockMovement |
| **Shipment** | Delivery orders, shipments, returns | DeliveryOrder, Shipment, ShipmentReturn |
| **Purchase** | Procurement lifecycle | PurchaseRequest, PurchaseOrder, GoodsReceipt, VendorInvoice, PurchaseReturn, VendorCatalog |
| **Finance** | Accounting, invoicing, payments | Account, AccountingPeriod, JournalEntry, SalesInvoice, CreditNote, Payment, Expense |
| **HR** | Human resources | Position, HrEmployee, Contract, LeavePolicy/Request/Balance, Attendance, PayrollConfig/Run, PerformanceCycle/Review, JobPosting/Application |
| **CRM** | Customer relationship management | Contact, Lead, Opportunity, Quote, Activity |
| **Notification** | Multi-channel notifications | TenantNotificationConfig, NotificationConfig, NotificationTemplate, NotificationPreference, WebhookConfig, InAppNotification |
| **Reporting** | Dynamic reports & exports | ReportDefinition, SavedReport, ScheduledReport |
| **Audit** | Immutable change tracking | AuditEntry (via `@audit` directive + JPA entity interceptor) |

## Multi-Tenancy

Schema-per-tenant isolation on PostgreSQL:

```
erp (database)
├── public           ← tenants table, flyway metadata
├── tenant_<uuid>    ← schema per tenant (all module tables)
└── tenant_<uuid>    ← another tenant
```

Every authenticated request targets a specific tenant via the `X-TENANT: <code>` header (e.g., `X-TENANT: TST`).

## Authentication

Three authentication modes per tenant:

1. **LOCAL** — username/password with bcrypt
2. **LDAP** — external directory authentication
3. **OIDC** — OpenID Connect (external IdP)

Bootstrap admin access via `X-API-KEY` header (generated at startup, logged to console).

### JWT Payload

```json
{
  "sub": "<userId>",
  "tenant": "<tenantId>",
  "username": "admin",
  "roles": ["<roleId>"],
  "permissions": ["SALES_ORDER:CREATE", "COMPANY:READ", ...],
  "authVersion": 2,
  "type": "access",
  "iat": 1771670763,
  "exp": 1771674363
}
```

## Getting Started

### Prerequisites

- Java 25+
- Docker & Docker Compose (for PostgreSQL)
- Maven 3.9+

### 1. Start Database

```bash
cd docker
docker compose up -d
```

### 2. Run Application

```bash
cd app
./mvnw spring-boot:run
```

### 3. Bootstrap Flow

1. Copy the API key from startup logs
2. Create a tenant: `POST /graphql` with `X-API-KEY` header
3. Register a user within the tenant
4. Assign a role to the user
5. Login to get JWT tokens
6. Use JWT for all subsequent operations

See [`FRONTEND_API_GUIDE.md`](./FRONTEND_API_GUIDE.md) for the complete API reference.

## Configuration

| Property | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/erp?sslmode=disable` | Database URL |
| `DB_USERNAME` | `myuser` | Database username |
| `DB_PASSWORD` | `mypassword` | Database password |
| `JWT_SECRET` | (embedded) | HMAC-SHA256 signing key |
| `JWT_EXPIRATION` | `3600` (1h) | Access token TTL in seconds |
| `JWT_REFRESH_EXPIRATION` | `604800` (7d) | Refresh token TTL in seconds |
| `BOOTSTRAP_API_KEY` | (auto-generated) | Fixed API key (empty = random per startup) |

## Project Structure

```
graphql-erp/
├── app/                    ← Spring Boot application, GraphQL schemas, resolvers
│   └── src/main/resources/
│       ├── schema/         ← *.graphqls files (18 schema files)
│       └── migration/      ← Flyway SQL migrations
│           ├── public/     ← V1-V4 (tenant table, auth settings)
│           └── tenant/     ← V1-V15 (per-tenant tables, permissions, roles)
├── core/                   ← Shared kernel + identity + reference data
│   ├── core-kernel/
│   ├── core-domain/
│   ├── core-application/
│   └── core-platform/
├── sales/
├── inventory/
├── shipment/
├── purchase/
├── finance/
├── hr/
├── crm/
├── notification/
├── reporting/
└── docker/
```

## License

GPL-3

