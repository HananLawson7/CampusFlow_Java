# CampusFlow — Smart Campus Resource Management & Workflow Automation System

> **Note:** This branch contains the full-stack mobile version of CampusFlow (React Native + Expo frontend, Java/Spark REST API backend). The original console-based Java prototype lives on the `main` branch.

CampusFlow digitises the process of booking and managing shared campus resources — classrooms, labs, and equipment — replacing manual paperwork, phone calls, and WhatsApp coordination with a role-based mobile app and a multi-stage digital approval workflow.

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React Native (Expo Router), TypeScript |
| Backend | Java 17, Spark Framework (spark-core), Gson |
| Database | PostgreSQL, accessed via JDBC |
| Build tool | Maven |

## Project Structure

```
CampusFlow/
├── campusflow-backend/
│   ├── pom.xml
│   └── src/main/java/
│       ├── config/        # DatabaseConfig (JDBC connection)
│       ├── model/         # User, Role, Resource, ProductRequest, PurchaseRequest, PurchaseOrder
│       ├── repository/    # JDBC data-access layer (one repository per entity)
│       └── api/           # Spark REST controllers (one per role) + ApiServer entry point
│
└── campusflow-app/
    ├── theme/              # Light/dark theme context
    ├── store/              # Auth context
    ├── api/client.ts       # Central fetch wrapper for all API calls
    └── src/app/            # Expo Router screens, organised by role
        ├── index.tsx        # Role selection
        ├── login.tsx
        ├── admin/
        ├── hod/
        ├── stores/
        ├── purchase/
        └── accounts/
```

## Database Schema

Five normalised PostgreSQL tables model the full resource-request lifecycle:

- **`users`** — 8 roles: `ADMIN`, `HOD`, `FACULTY`, `STUDENT`, `STORES`, `PURCHASE`, `ACCOUNTS`, `BOARD_MEMBERS`
- **`resources`** — inventory master (name, category, stock quantity, unit price)
- **`product_requests`** — HOD → Stores requests
- **`purchase_requests`** — Stores → Purchase escalations (raised when stock is insufficient)
- **`purchase_orders`** — Purchase → Accounts orders, tracked through billing and payment

### Request Lifecycle

```
HOD submits request (PENDING)
  → Stores checks stock:
      sufficient    → deduct inventory → FULFILLED_FROM_STOCK
      insufficient  → create purchase_request → FORWARDED_TO_PURCHASE
  → Purchase creates a Purchase Order (PENDING_PO → PO_ISSUED)
  → Accounts processes payment (CREATED → BILLED → PAID)
  → On payment: inventory is replenished and the original request
    reopens as PENDING, ready to be reprocessed by Stores
```

## Setup

### 1. Database

Create a PostgreSQL database (default name used throughout: `campus_flow`) and run the schema script to create the five tables and seed seed data (8 test users, one per role, plus sample resources).

### 2. Backend

```bash
cd campusflow-backend
```

Set your PostgreSQL credentials in `config/DatabaseConfig.java` (or via environment variables `CAMPUSFLOW_DB_URL`, `CAMPUSFLOW_DB_USER`, `CAMPUSFLOW_DB_PASSWORD`).

Run the API server from `api/ApiServer.java` (via your IDE, or):

```bash
mvn compile exec:java -Dexec.mainClass="api.ApiServer"
```

The server starts on `http://0.0.0.0:4567`.

### 3. Frontend

```bash
cd campusflow-app
npm install --legacy-peer-deps
npx expo install expo-secure-store
```

Update `api/client.ts` with your machine's **LAN IP address** (not `localhost`) so a physical device on the same Wi-Fi network can reach the backend:

```ts
const BASE_URL = "http://<your-lan-ip>:4567/api";
```

Then start the app:

```bash
npx expo start
```

Scan the QR code with Expo Go on your phone (same Wi-Fi network as your machine).

## Test Credentials

| Username | Password | Role |
|---|---|---|
| `sys_admin` | `adminpass` | ADMIN |
| `cs_hod` | `hodpass` | HOD |
| `prof_smith` | `profpass` | FACULTY |
| `st_alex` | `studpass` | STUDENT |
| `stores_mgr` | `storespass` | STORES |
| `purchase_lead` | `purchasepass` | PURCHASE |
| `acc_officer` | `accpass` | ACCOUNTS |
| `board_chair` | `boardpass` | BOARD_MEMBERS |

## Implemented Role Screens

Full dashboard functionality is currently implemented for **five** of the eight roles:

- **Admin** — create user (role-conditional form), view all users, deactivate user, reset password
- **HOD** — submit resource request, view own requests with live status
- **Stores** — view pending requests (conditional "Process" / "Forward to Purchase" based on live stock), view inventory
- **Purchase** — view pending purchase requests, create purchase orders
- **Accounts** — view created/billed/paid orders, mark billed, mark paid

Faculty, Student, and Board Members accounts authenticate successfully but do not yet have dedicated dashboard screens beyond login.

## Known Limitations

- Backend and mobile client communicate over the local network only; no cloud deployment yet
- Purchase order cost is entered manually rather than computed from resource unit price
- No push notifications — status changes must be checked manually in-app
- Tested against a single seeded dataset; no multi-department or concurrency load testing performed

## Future Scope

- Push notifications for status changes
- Vendor-management module with automatic PO cost calculation
- Multi-department / multi-institution support
- Cloud deployment for off-campus access
- Low-stock prediction based on historical request patterns
