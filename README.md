# JWT-Secured REST API

A REST API built with Java and Spring Boot that demonstrates stateless JWT authentication, role-based access control, and resource-ownership authorization. It exposes endpoints for registering and authenticating users, managing user accounts, and managing products, with MySQL persistence through Spring Data JPA.

I made this project to practice developing REST APIs with Spring Boot and implementing authentication and authorization using JWT.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
  - [Package Responsibilities](#package-responsibilities)
- [Branch Structure](#branch-structure)
  - [Merge Strategy](#merge-strategy)
- [Authentication and Authorization](#authentication-and-authorization)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication)
  - [Users](#users)
  - [Products](#products)
- [Request and Response Examples](#request-and-response-examples)
  - [Authentication](#authentication-1)
  - [Users](#users-1)
  - [Products](#products-1)
- [Setup](#setup)
  - [Prerequisites](#prerequisites)
  - [Generate an RSA Key Pair](#generate-an-rsa-key-pair)
- [Run the Application](#run-the-application)
- [Build and Run the JAR](#build-and-run-the-jar)
- [Docker and MySQL](#docker-and-mysql)
  - [Assigning an Administrator Role for Testing](#assigning-an-administrator-role-for-testing)
- [Recommended Changes for Production](#recommended-changes-for-production)
  - [Additional Recommendation](#additional-recommendation)
- [References](#references)
  - [Tutorials](#tutorials)

## Features

- **JWT authentication**: register an account and log in to receive a signed access token.
- **Stateless request authentication**: Spring Security validates bearer tokens on protected endpoints.
- **Role-based authorization**: restrict administrative operations to users with the `ADMIN` role.
- **Resource-ownership authorization**: users can retrieve their own account details and update their own password.
- **User management**: create, retrieve, list, update the password of, and delete user accounts, subject to access rules.
- **Product management**: create, retrieve, search, update, and delete products, subject to access rules.
- **Input validation**: Jakarta Bean Validation constraints validate request bodies and selected request parameters.
- **Password hashing**: passwords are encoded with BCrypt before being stored.
- **Relational persistence**: Spring Data JPA stores users, roles, and products in MySQL.
- **RSA-signed JWTs**: an RSA key pair is used to sign and verify access tokens.

## Tech Stack

- Java (version: 25)
- Spring Boot (version: 4.1.1)
    - Spring Web
    - Spring Security
    - OAuth2 Resource Server
    - Spring Data JPA
    - MySQL Driver
    - Docker Compose Support
    - Testcontainers
    - Validation
- Maven
- Docker Compose
- OpenSSL (for generating the RSA key pair)
- Postman (for manual API testing)

## Project Structure

The following tree covers `src/main/java/`:

```text
com.matt1479.jwtsecuredrestapi/
├── JwtSecuredRestApiApplication.java
├── auth/
│   ├── AuthController.java
│   ├── TokenService.java
│   └── dto/
│       ├── LoginRequest.java
│       ├── RegisterRequest.java
│       └── TokenResponse.java
├── config/
│   ├── RsaKeyProperties.java
│   └── SecurityConfig.java
├── products/
│   ├── Product.java
│   ├── ProductController.java
│   ├── ProductRepository.java
│   ├── ProductService.java
│   └── dto/
│       ├── ProductRequest.java
│       └── ProductResponse.java
├── security/
│   └── AuthorizationService.java
└── users/
    ├── CustomUserDetailsService.java
    ├── Role.java
    ├── User.java
    ├── UserController.java
    ├── UserRepository.java
    ├── UserService.java
    └── dto/
        ├── UpdatePasswordRequest.java
        ├── UserRequest.java
        └── UserResponse.java
```

### Package Responsibilities

- `JwtSecuredRestApiApplication`: Spring Boot application entry point; enables binding of RSA key properties.
- `auth`: login and registration endpoints, access-token creation, and authentication request/response DTOs.
  - `dto`: records used to validate incoming authentication requests and shape responses.
- `config`: Spring Security configuration, JWT encoder/decoder setup, password encoding, and RSA key configuration.
- `products`: product entity, repository, service-layer operations, REST endpoints, and product DTOs.
  - `dto`: validated product request and product response records.
- `security`: authorization helper methods used to check whether the authenticated user owns a requested account.
- `users`: user entity, role enum, repository, service-layer operations, REST endpoints, and the `UserDetailsService` implementation.
  - `dto`: user request/response records and the password-update request record.

## Branch Structure

The project was developed in feature-oriented branches:

1. `chore/setup`: project configuration and dependencies.
2. `feat/users`: user entity, repository, service, and controller.
3. `feat/jwt-authentication`: registration, login, token generation, and JWT validation.
4. `feat/products`: product entity, repository, service, and controller.
5. `feat/authorization`: role-based and resource-ownership access restrictions.
6. `docs/readme`: README documentation.

### Merge Strategy

Merge each feature branch into `main` before starting the next branch. Use `--no-ff` to ensure Git creates a merge commit and `-m` to set its message:

```bash
git switch main # switch=checkout
git merge --no-ff branch-name -m "type(scope): description"
```

The message format follows [Conventional Commits](https://www.conventionalcommits.org/).

## Authentication and Authorization

- **Unauthenticated endpoints**: `POST /api/auth/login` and `POST /api/auth/register`.
- **Authenticated endpoints**: user and product endpoints require a valid bearer token.
- **`USER` role**: can read product resources and access their own user details; can update their own password.
- **`ADMIN` role**: can manage products and perform administrative user operations.
- **Ownership checks**: user-detail access is restricted to the account owner or an administrator. Password updates are restricted to the account owner.

Newly registered users receive the `USER` role. The public registration endpoint does not let callers assign themselves the `ADMIN` role.

Send the token in the `Authorization` header when calling a protected endpoint:

```http
Authorization: Bearer <access-token>
```

## API Endpoints

`{id}` represents a user or product ID. Request-body constraints are described in the examples below.

### Authentication

<details>
<summary>View endpoints</summary>

These endpoints are public

| Method    | Endpoint              | Description   |
|-----------|-----------------------|---------------|
| POST      | /api/auth/login       | Log In        |
| POST      | /api/auth/register    | Register      |

</details>

### Users

<details>
<summary>View endpoints</summary>

All endpoints below require authentication. Access restrictions are shown in the last column.

| Method    | Endpoint                          | Description               | Access                            |
|-----------|-----------------------------------|---------------------------|-----------------------------------|
| POST      | /api/users                        | Create a user             | Admin                             |
| GET       | /api/users/{id}                   | Get a user by ID          | Admin or owner of that ID         |
| GET       | /api/users?username=example       | Get a user by username    | Admin or owner of that username   |
| GET       | /api/users                        | Get all users             | Admin                             |
| PATCH     | /api/users/{id}/updatePassword    | Update a user's password  | Owner of that ID                  |
| DELETE    | /api/users/{id}                   | Delete a user by ID       | Admin                             |

</details>

### Products

<details>
<summary>View endpoints</summary>

All product endpoints require authentication. Product reads are available to authenticated users; writes are restricted to administrators.

| Method    | Endpoint                          | Description                       | Access                |
|-----------|-----------------------------------|-----------------------------------|-----------------------|
| POST      | /api/products                     | Create a product                  | Admin                 |
| GET       | /api/products/{id}                | Get a product by ID               | Authenticated User    |
| GET       | /api/products?title=example       | Get a product by exact title      | Authenticated User    |
| GET       | /api/products?description=example | Get products by exact description | Authenticated User    |
| GET       | /api/products                     | Get all products                  | Authenticated User    |
| PUT       | /api/products/{id}                | Update a product by ID            | Admin                 |
| DELETE    | /api/products/{id}                | Delete a product by ID            | Admin                 |

The title and description lookups currently use exact matches, not partial-text search.

</details>

## Request and Response Examples

Examples use JSON. Validation failures depend on the application's configured Spring validation/error handling.

### Authentication

<details>

<summary>View examples</summary>

1. Log In.

`POST /api/auth/login`

```json
{
    "username": "example",
    "password": "examplePasswd"
}
```
Both fields must be non-blank.

Example response:

```json
{
    "accessToken": "example",
    "expiresInSeconds": 3600
}
```

`expiresInSeconds` is derived from `jwt.ttl-minutes` in the application configuration. The example value assumes a 60-minute token lifetime.

2. Register.

`POST /api/auth/register`

```json
{
    "username": "example",
    "password": "examplePasswd"
}
```

Constraints:
- `username`: required, 3-15 characters.
- `password`: required, 8-20 characters.

Example response:

```json
{
    "id": 123,
    "username": "example",
    "roles": ["USER"]
}
```

</details>

### Users

<details>

<summary>View examples</summary>

1. Create user.

`POST /api/users` - `ADMIN` only.

```json
{
    "username": "example",
    "password": "examplePasswd"
}
```

Constraints:
- `username`: required, 3-15 characters.
- `password`: required, 8-20 characters.

The response has the same shape as the registration response. New users receive the `USER` role.

2. Get user by ID.

`GET /api/users/123`.

Access: `Admin` or the owner of user ID `123`.

Example response:

```json
{
    "id": 123,
    "username": "example",
    "roles": ["USER"]
}
```

The ID must be at least `1`.

3. Get user by username.

`GET /api/users?username=example`.

Access: `Admin` or the owner of username `example`.

The `username` query parameter must not be blank. The response has the same shape as the get-user-by-ID response.

4. Get all users.

`GET /api/users` - `ADMIN` only.

Example response:

```json
[
    {
        "id": 123,
        "username": "example",
        "roles": ["USER"]
    },
    {
        "id": 456,
        "username": "anotherExample",
        "roles": ["ADMIN", "USER"]
    }
]
```

5. Update password.

`PATCH /api/users/123/updatePassword` - owner of user ID `123` only.

```json
{
    "password": "newExamplePasswd"
}
```

The password must be non-blank and 8-20 characters long. On success, the endpoint returns `204 No Content` with no response body.

6. Delete a user.

`DELETE /api/users/123` - `ADMIN` only.

On success, the endpoint returns `204 No Content` with no response body.

</details>

### Products

<details>

<summary>View examples</summary>

1. Create product.

`POST /api/products` - `ADMIN` only.

```json
{
    "title": "example",
    "description": "example description",
    "price": 12.34
}
```

Constraints:
- `title`: required, at most 75 characters.
- `description`: required, at most 750 characters.
- `price`: required and greater than zero.

Example response:

```json
{
    "id": 123,
    "title": "example",
    "description": "example description",
    "price": 12.34
}
```

Product titles must be unique.

2. Get a product by ID.

`GET /api/products/123`.

Available to any authenticated user.

Example response:

```json
{
    "id": 123,
    "title": "example",
    "description": "example description",
    "price": 12.34
}
```

3. Get a product by title.

`GET /api/products?title=example`.

Available to any authenticated user. The `title` parameter must not be blank. The lookup is an exact match and returns one product or `404 Not Found`.

4. Get products by description.

`GET /api/products?description=example`.

Available to any authenticated user. The `description` parameter must not be blank. The lookup is an exact match and returns a list, which may be empty.

5. Get all products.

`GET /api/products`.

Available to any authenticated user.

Example response:

```json
[
    {
        "id": 123,
        "title": "example",
        "description": "example description",
        "price": 12.34
    },
    {
        "id": 456,
        "title": "anotherExample",
        "description": "example description",
        "price": 45.67
    }
]
```

6. Update a product.

`PUT /api/products/123` - `ADMIN` only.

```json
{
    "title": "exampleUpdated",
    "description": "updated description",
    "price": 11.22
}
```

The request uses the same validation constraints as product creation. Product titles must remain unique. The response contains the updated product.

7. Delete a product.

`DELETE /api/products/123` - `ADMIN` only.

On success, the endpoint returns `204 No Content` with no response body.

</details>

## Setup

### Prerequisites

Install or provide:
- Java 25
- Docker with the Docker Compose plugin
- OpenSSL

Check that Java and Docker are available:

```bash
java -version
docker --version
docker compose version
```

### Generate an RSA Key Pair

From the project root, run:

```bash
mkdir -p src/main/resources/certs
cd src/main/resources/certs

openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
rm keypair.pem
```

This creates:
- `public.pem` - used to verify JWT signatures.
- `private.pem` - used to sign JWTs.

Ensure the key locations match the `rsa` properties in the application's configuration. Do not commit private keys or use development keys in production.

For background, see [Securing REST APIs in Spring Boot 4 with JWT](https://ajinkya-mundankar.medium.com/securing-rest-apis-in-spring-boot-4-with-jwt-the-2026-way-no-custom-filters-5d36ab500e81) # Step 2 - Generate an RSA key pair.

## Run the Application

From the project root, run:

```bash
./mvnw spring-boot:run
```

Make sure the database is running and the application configuration points to it.

## Build and Run the JAR

Start the MySQL container - from the project root, run:

```bash
docker compose up -d
```

The Compose service is referenced as `mysql` in the commands below. Check `compose.yaml` for the configured database, user, and password.

Build the JAR file:

```bash
./mvnw clean package
```

Run the JAR file:

```bash
java -jar target/jwt-secured-rest-api-0.0.1-SNAPSHOT.jar
```

The JAR file name may differ if the Maven project version or artifact name changes.

## Docker and MySQL

Run the MySQL CLI inside the Docker container:

```bash
docker compose exec mysql mysql -u root -p
```

You will need to provide the MySQL root password defined in [compose.yaml](compose.yaml).

### Assigning an Administrator Role for Testing

Newly registered and admin-created users receive only the `USER` role. To test admin-only endpoints, assign the `ADMIN` role to an existing user in the `user_roles` table. First inspect the actual schema and username-to-ID mapping in your local database; then insert the role for the correct user ID. For example:

```sql
SELECT id, username FROM user;
SELECT user_id, role FROM user_roles;
```

If your MySQL version treats `user` as a reserved word, quote the table name with backticks: `` `user` ``.

Only do this in a local development database. Never expose administrative database credentials or grant roles based on untrusted user input.

## Recommended Changes for Production

- Store keys in a secrets manager, instead of the classpath.
- Keep private keys and credentials out of version control.
- Implement refresh tokens.
- Configure CORS if browser-based clients use a different origin.
- Use `BigDecimal` instead of `Double` for currency.

Refer to [Securing REST APIs in Spring Boot 4 with JWT](https://ajinkya-mundankar.medium.com/securing-rest-apis-in-spring-boot-4-with-jwt-the-2026-way-no-custom-filters-5d36ab500e81) # What you'd change for production.

### Additional Recommendation

- Ensure error responses do not expose internal details.

## References

- Documentation: [OAuth 2.0 Resource Server JWT :: Spring Security](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html).
- [Conventional Commits](https://www.conventionalcommits.org/).
- [HTTP Cats: HTTP status code reference](https://http.cat/).
- [Spring Initializr](https://start.spring.io/).

### Tutorials

Basically all the tutorials that allowed me to get to this point:

0. [Quickstart](https://spring.io/quickstart).
1. [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot).
2. [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service).
3. [Consuming a RESTful Web Service](https://spring.io/guides/gs/consuming-rest).
4. [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content).
5. [Securing a Web Application](https://spring.io/guides/gs/securing-web).
6. [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa).
7. [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql).
8. JWT article: [Securing REST APIs in Spring Boot 4 with JWT](https://ajinkya-mundankar.medium.com/securing-rest-apis-in-spring-boot-4-with-jwt-the-2026-way-no-custom-filters-5d36ab500e81).
