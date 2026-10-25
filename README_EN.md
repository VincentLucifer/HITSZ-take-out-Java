# Take-Out Backend · Java Food-Ordering Services

[简体中文](README.md) | [English](README_EN.md)

Java backend practice based on the **Sky Take Out teaching project**, organising dishes, set meals, carts, orders, notifications and business reporting for merchant administration and customer ordering.

Built with **Spring Boot, MyBatis, MySQL and Redis**, with a focus on layered business logic, order-state transitions, cache/database coordination and reusable infrastructure.

## Business modules

| Area | Contents |
| --- | --- |
| Administration | Employees, categories, dishes/flavours, set meals and shop status |
| Customer services | WeChat login integration, addresses, browsing and shopping carts |
| Orders | Submission, lookup, cancellation, reordering, confirmation, rejection, delivery, completion and reminders |
| Notifications | WebSocket order alerts and reminders |
| Workspace | Order, dish, set-meal and business overviews |
| Statistics | Turnover, user growth, order metrics and top-ten sales |
| Reports | Template-based Excel business-data export |

The current course payment demonstration updates payment state locally. WeChat payment, refund and callback utilities/interfaces are also retained for integration with appropriately configured external services.

## Engineering design

### Modules and layers

| Module | Responsibility |
| --- | --- |
| `sky-common` | Shared results, exceptions, constants, JWT/HTTP/OSS utilities and property classes |
| `sky-pojo` | Entities, DTOs and VOs |
| `sky-server` | Controllers, services, mappers, configuration, tasks and WebSocket |

Requests follow `Controller → Service → Mapper → MySQL`, separating API inputs, business rules and persistence.

### JWT and request context

Separate admin and customer JWT interceptors control access by path. `BaseContext` carries the current user ID into business operations and audit-field handling.

### Caching and updates

Customer dish lists check Redis by category, returning cached data or loading from the database and populating the cache. Admin dish changes invalidate related entries. Redis also stores shop status; set-meal queries express caching through `@Cacheable`.

### AOP audit fields

`@AutoFill` identifies Mapper operation types. An aspect obtains the current time and user ID and uses reflection to populate creation/update timestamps and operators, reducing repeated assignments.

### Orders and messages

Order submission uses a transaction annotation to coordinate orders, details and cart operations. Services handle cancellation, confirmation and delivery according to order states, and send order/reminder messages through `/ws/{sid}`.

`OrderTask` defines scheduled processing methods for overdue unpaid orders and delivery orders. Reporting uses Apache POI to populate a template and produce Excel output.

## Stack

| Technology | Role |
| --- | --- |
| Spring Boot 2.7.3 / Spring MVC | Application startup and HTTP APIs |
| MyBatis / MySQL / Druid | Mapping, relational storage and connection pooling |
| Redis / Spring Cache | Dish caching, shop status and cache annotations |
| JWT | Admin and customer authentication |
| Spring AOP | Audit-field population |
| WebSocket | Server notifications |
| PageHelper | Pagination |
| Apache POI | Excel reports |
| Knife4j / Swagger | Separate admin/customer API documentation |
| Maven | Multi-module builds and dependencies |

## Local setup

Prepare a Spring Boot 2.7-compatible JDK, Maven, MySQL and Redis, and import the course business schema and initial data.

```bash
git clone https://github.com/VincentLucifer/HITSZ-take-out-Java.git
cd HITSZ-take-out-Java
mvn -pl sky-server -am package
java -jar sky-server/target/sky-server-1.0-SNAPSHOT.jar
```

Alternatively, run `com.sky.SkyApplication` from an IDE. The default configuration activates the `dev` profile and uses HTTP port `8080`.

Supply the properties referenced by `application.yml` in local configuration, for example `sky-server/src/main/resources/application-dev.yml`:

| Prefix | Contents |
| --- | --- |
| `sky.datasource` | Driver, MySQL host, port, database and credentials |
| `sky.redis` | Redis host, port, password and database |
| `sky.alioss` | OSS endpoint, bucket and local credentials |
| `sky.wechat` | WeChat application, merchant, certificates and callbacks |
| `sky.shop.address` | Shop address |
| `sky.baidu.ak` | Map-service configuration |
| `sky.jwt` | Admin and customer JWT settings |

Use your own local credentials and configure external services for the login, upload, address and refund flows you use.

After startup:

- API documentation: `http://localhost:8080/doc.html`
- Customer dish list: `GET /user/dish/list?categoryId=<id>` (customer token required)
- Shop status: `GET /user/shop/status`
- Notifications: `ws://localhost:8080/ws/<sid>`

## Suggested code entry points

1. `controller/user/DishController.java` and admin `DishController` for cache reads and invalidation.
2. `aspect/AutoFillAspect.java` for annotations, aspects and reflection.
3. `service/impl/OrderServiceImpl.java` for order rules and state transitions.
4. `service/impl/ReportServiceImpl.java` for aggregated statistics and Excel export.
5. `config/WebMvcConfiguration.java` for JWT interceptors and dual API documentation.

Paths are relative to `sky-server/src/main/java/com/sky/`.

## Background

Course and engineering practice built on the Sky Take Out teaching project. The repository retains the original `sky-*` module names and focuses on backend business logic and integration of common Java service components.
