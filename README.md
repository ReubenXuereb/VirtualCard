# Nium Task Virtual Card Issuance Platform
### Endpoints
* If you are using Postman go to ./docs -> Nium.postman_collection.json and import the endpoints.

| Method | Endpoint                     | Description                    |
|--------|------------------------------|--------------------------------|
| POST   | /api/cards/create            | Creates a new Card.            |
| GET    | /api/cards/{id}              | Gets Card Details              |
| GET    | /api/cards/{id}/transactions | Gets Card Transaction History  |
| POST   | /api/cards/{id}/spend        | Spend money with specific Card |
| POST   | /api/cards/{id}/topup        | Topup money with specific Card |


## Layered Architecture
* The project uses a **modular, layered architecture** with clear separation between the **API layer** and the **Core Service layer**.
````
VirtualCard/
├── api/ → Web, HTTP, Controllers, DTOs
├── service/ → Business logic, Entities, Repositories
└── pom.xml → Parent for dependency management
````

* Each module focuses on a single responsibility. The `api` module handles incoming HTTP requests and validation, while the `service` module encapsulates domain logic and persistence. This makes the system easier to reason about and test independently.
* Changes to the REST endpoints (for example adding a new field to a response) does not affect business logic or database schemas encouraging both modules to be loosley coupled.
* The `service` module can be reused by another interface (for example, a gRPC service or CLI tool) without depending on Spring MVC.

## Concurrency Handling — Optimistic Locking with `@Version`

#### Why Optimistic Locking is Better Here:

- **Non-blocking performance** — concurrent reads remain fast.
- **Scalability** — suitable for systems where conflicts are rare.
- **Simplicity** — implemented via `@Version` on the entity, no manual locking needed.
- **Retry-friendly** — failed updates can be retried safely.

## Trade-offs Made Due to Time Constraints

- **Database Choice**
  - Used in-memory H2 as it simplifies testing and setup
- **Security**
  - No authentication or rate-limiting as i focused more on core features
- **DTO mapping**
  - Manual mapping instead of a MapStruct
- **Tests**
  - Limited integration tests

## Potential Improvements
- **Add Pagination and Sorting to Transaction History Endpoint**
  - This is very useful when transaction lists become large.
- **Integrate Messaging Queues(Kakfa)**
  - To asynchronously process transactions and emit audit logs
- **CI/CD & Containerization**
  - Dockerize the service and deploy it to a cloud environment
- **Add Authentication & Rate Limiting**

## Learning Strategy
- **Optimistic Locking** — read JPA documentation and reference implementations to understand version-based concurrency control.
