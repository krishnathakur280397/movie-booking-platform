# Movie Booking Platform (Spring Boot + H2)

This project implements a minimal backend for:

- Viewing available movie shows
- Booking tickets with basic seat-availability protection

It uses **Spring Boot**, **Spring Web**, **Spring Data JPA**, and an in-memory **H2** database.

## Architecture (production-grade shape)

### Components

- **API layer (Controllers)**
  - Accepts HTTP requests, validates input, returns DTO responses
  - Calls **services only** (no repository calls from controllers)
- **Service layer**
  - Business rules and orchestration
  - Transaction boundaries (booking flow is transactional)
- **Repository layer**
  - Persistence through Spring Data JPA
- **Database**
  - H2 in-memory for local/dev
  - In production, swap H2 for Postgres/MySQL (same JPA model)

### Working scenario (end-to-end interaction)

1. User opens the app and calls `GET /api/shows` to see shows.
2. User selects a show and calls `POST /api/bookings` with `{ showId, seats, customerName }`.
3. The booking service:
   - Starts a transaction
   - Loads the show row with a **pessimistic write lock**
   - Checks `availableSeats`
   - Decrements seats and creates a booking
   - Commits the transaction

This ensures two concurrent bookings do not oversell seats.

## APIs

Base URL: `http://localhost:8080`

### Shows

- `GET /api/shows`
  - Optional query params:
    - `from` (ISO-8601 datetime)
    - `to` (ISO-8601 datetime)

- `GET /api/shows/{id}`

### Bookings

- `POST /api/bookings`
  - Body:
    - `showId` (number)
    - `customerName` (string)
    - `seats` (number, min 1)

- `GET /api/bookings`
  - Optional: `showId` to filter bookings for a show

- `GET /api/bookings/{id}`

### Example cURL

List shows:

```bash
curl -s http://localhost:8080/api/shows
```

Get show:

```bash
curl -s http://localhost:8080/api/shows/1
```

Book tickets:

```bash
curl -s -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"showId":1,"customerName":"Krishna","seats":2}'
```

Get booking:

```bash
curl -s http://localhost:8080/api/bookings/1
```

## Database model

### Tables

- `movies`
  - `id`, `title`
- `shows`
  - `id`, `movie_id`, `screen`, `show_time`, `total_seats`, `available_seats`
- `bookings`
  - `id`, `show_id`, `customer_name`, `seats_booked`, `created_at`

### Consistency

- Seat availability is stored on the show (`availableSeats`).
- Booking uses:
  - `@Transactional` in the service
  - `PESSIMISTIC_WRITE` lock when loading a show for booking

## Scale, security, payments, integrations, availability

### Scale

- **Read scaling**
  - We can add caching for show listings (Redis, short TTL) to reduce DB load.
  - We can use pagination and filters for `GET /api/shows`.
- **Write scaling (bookings)**
  - We can keep booking strongly consistent (single write DB primary).
  - We can use DB transactions and locking (or optimistic locking with retries) to avoid overselling.
  - We can partition/shard later by city/theatre if the dataset grows very large.

### Security

- Current config permits all requests for simplicity.
- Production-ready approach:
  - We can use OAuth2/JWT (Spring Security) for user identity.
  - Role-based access:
    - `ADMIN` can create movies/shows
    - `USER` can book
  - We can rate limit booking endpoints, input validation, audit logging.
  - we can implant TLS everywhere.

### Payments

In production flow we need to make sure “booking confirmation” does not happen before payment success. Typical approach:

- Create a **payment intent** (Stripe/Razorpay/etc).
- Create a temporary **hold** (seat hold with expiry) or “pending booking”.
- On payment webhook success:
  - Confirm booking (finalize and decrement seats)
- On timeout/failure:
  - Release hold

Add **idempotency keys** to prevent duplicate bookings on retries.

### Integrations

- We can use payment provider webhooks
- We can configure proper notifications (email/SMS) via async jobs/queues
- Also, we can use Analytics/event stream (Kafka/SQS) for tracking 

### Availability & reliability

- We can run multiple application instances behind a load balancer.
- Implement Health checks + readiness probes.
- We can use managed DB with backups.
- Observability:
  - Metrics (booking success/fail/conflicts)
  - Centralized logs
  - Tracing for latency

## Tech choices

- Java 21
- Spring Boot 3
- Spring Web + Validation
- Spring Data JPA
- MySQL (ACID transactions + row-level locking = easiest way to prevent overselling seats)
- Mockito/JUnit for unit tests

## Hosting approach (production)

- Containerize with Docker.
- Deploy to:
  - AWS ECS/EKS
- Use a managed database (RDS Postgres/MySQL).
- Add Redis for caching and a queue for async workflows.

## H2 console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:moviebooking`
- User: `sa`
- Password: *(empty)*
