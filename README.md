# JWT-Secured REST API

A RESTful API written in Java, using Spring Framework, with JWT-based authentication, role and resource ownership-based authorization.

The API supports CRUD operations for users and products.

The application uses Spring Data JPA and a MySQL database for data persistence.

## Tech Stack

- Java (version: `25`)
- Spring Boot (version: `4.1.1`)
    - Spring Web
    - Spring Security
    - OAuth2 Resource Server
    - Spring Data JPA
    - MySQL Driver
    - Docker Compose Support
    - Testcontainers
    - Validation
- Maven
- Postman

## Features

- RESTful API using Spring's `@RestController`
- Data persistence using Spring Data JPA
- MySQL relational database
- users; User management (CRUD operations for users)
- products; Product management (CRUD operations for products)
- auth; JWT-based authentication
- Role-based authorization
- Resource ownership-based authorization

## Project/Package Structure

<details>

<summary>View</summary>

`src/main/java/`

```
com.matt1479.jwtsecuredrestapi/
|-- JwtSecuredRestApiApplication.java
|-- auth
|   |-- AuthController.java
|   |-- TokenService.java
|   `-- dto
|       |-- LoginRequest.java        
|       |-- RegisterRequest.java     
|       `-- TokenResponse.java  
|-- config
|   |-- RsaKeyProperties.java   
|   `-- SecurityConfig.java     
|-- products
|   |-- Product.java
|   |-- ProductController.java  
|   |-- ProductRepository.java  
|   |-- ProductService.java     
|   `-- dto
|       |-- ProductRequest.java      
|       `-- ProductResponse.java     
|-- security
|   `-- AuthorizationService.java    
`-- users
    |-- CustomUserDetailsService.java
    |-- Role.java
    |-- User.java
    |-- UserController.java
    |-- UserRepository.java
    |-- UserService.java
    `-- dto
        |-- UpdatePasswordRequest.java
        |-- UserRequest.java
        `-- UserResponse.java
```

- `JwtSecuredRestApiApplication`: application entry point
- `auth`: TODO (short description)
    - `dto`: request and response records used for data transfer
- `config`: TODO (short description)
- `products`: product-related functionality, including Product entity, repository, service, and controller
    - `dto`: request and response records used for data transfer
- `security`: TODO (short description)
- `users`: user-related functionality, including User entity, repository, service, and controller (as well as Role enum and CustomUserDetailsService)
    - `dto`: request and response records used for data transfer

</details>

## Branch Structure

1. `chore/setup`: project configuration and dependencies.
2. `feat/users`: User entity, repository, service, and controller.
3. `feat/jwt-authentication`: JWT authentication (login, register, token generation, and JWT validation).
4. `feat/products`: Product entity, repository, service, and controller.
5. `feat/authorization`: access restrictions; role and resource ownership checks.
6. `docs/readme`: README file.

### Strategy/Principle(s)

Merge each branch into `main` before starting the next.

Specify the merge commit message:

```bash
git switch main # switch=checkout
git merge --no-ff branch-name -m "type(scope): description";
```

- `--no-ff` ensures Git creates a merge commit
- `-m` sets its message

## API Endpoints

To test the endpoints that require the admin role, you will need to manually insert a role of `"ADMIN"` to an existing user using SQL. See: [Docker and MySQL](#docker-and-mysql) to learn how run the MySQL CLI inside a Docker container.

### Auth

<details>
<summary>View endpoints</summary>

Endpoints accessible by anyone.

| Method    | Endpoint              | Description   |
|-----------|-----------------------|---------------|
| POST      | /api/auth/login       | Log In        |
| POST      | /api/auth/register    | Register      |

</details>

### Users

<details>
<summary>View endpoints</summary>

Endpoints accessible by authenticated users.

| Method    | Endpoint                          | Description           | Access                |
|-----------|-----------------------------------|-----------------------|-----------------------|
| POST      | /api/users                        | Create user           | Admin                 |
| GET       | /api/users/{id}                   | Get user by ID        | Admin or owner of ID  |
| GET       | /api/users?username=example       | Get user by username  | Admin or owner of ID  |
| GET       | /api/users                        | Get all users         | Admin                 |
| PATCH     | /api/users/{id}/updatePassword    | Update password       | Owner of ID           |
| DELETE    | /api/users/{id}                   | Delete user by ID     | Admin                 |

</details>

### Products

<details>
<summary>View endpoints</summary>

Endpoints accessible by authenticated users.

| Method    | Endpoint                          | Description                   | Access        |
|-----------|-----------------------------------|-------------------------------|---------------|
| POST      | /api/products                     | Create product                | Admin         |
| GET       | /api/products/{id}                | Get product by ID             | Admin, User   |
| GET       | /api/products?title=value         | Get product by title          | Admin, User   |
| GET       | /api/products?description=value   | Get product by description    | Admin, User   |
| GET       | /api/products                     | Get all products              | Admin, User   |
| PUT       | /api/products/{id}                | Update product by ID          | Admin         |
| DELETE    | /api/products/{id}                | Delete product by ID          | Admin         |

</details>

## Examples

### Auth

<details>

<summary>View examples</summary>

Endpoints accessible by anyone.

1. Log In.

Method: `POST`.

Endpoint: `/api/auth/login`.

Request body:

```json
{
    "username": "example",      // @NotBlank
    "password": "examplePasswd" // @NotBlank
}
```

`@NotBlank`: can't be empty or consisting of only whitespace characters.

Response:

```json
{
    "accessToken": "example",
    "expiresInSeconds": 3600
}
```

`expiresInSeconds` is based on the value of jwt.ttl-minutes, defined in application.yaml.

2. Register.

Method: `POST`.

Endpoint: `/api/auth/register`.

Request body:

```json
{
    "username": "example",      // @NotBlank @Size(min = 3, max = 15)
    "password": "examplePasswd" // @NotBlank @Size(min = 8, max = 20)
}
```

`@NotBlank`: can't be empty or consisting of only whitespace characters.
`@Size(min, max)`: must be between min and max, including both ends.

Response:

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

Endpoints accessible by authenticated users.

1. Create user.

Method: `POST`.

Endpoint: `/api/users`.

Access: `Admin`.

Request body:

```json
{
    "username": "example",      // @NotBlank @Size(min = 3, max = 15)
    "password": "examplePasswd" // @NotBlank @Size(min = 8, max = 20)
}
```

Response:

```json
{
    "id": 123,
    "username": "example",
    "roles": ["USER"]
}
```

2. Get user by ID.

Method: `GET`.

Endpoint: `/api/users/{id}`.

Access: `Admin or owner of ID`.

Path variable:

`id`, greater than or equal to `1` (`@Min(1)`)

Response:

```json
{
    "id": 123,
    "username": "example",
    "roles": ["USER"]
}
```

3. Get user by username.

Method: `GET`.

Endpoint: `/api/users?username=example`.

Access: `Admin or owner of username`.

Request parameter:

`username`, `@NotBlank`

Response:

```json
{
    "id": 123,
    "username": "example",
    "roles": ["USER"]
}
```

4. Get all users.

Method: `GET`.

Endpoint: `/api/users`.

Access: `Admin`.

Response:

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

Method: `PATCH`.

Endpoint: `/api/users/{id}/updatePassword`.

Access: `Owner of ID`.

Path variable:

`id`.

Request body:

```json
{
    "password": "examplePasswd" // @NotBlank @Size(min = 8, max = 20)
}
```

Response:

`void`.

HTTP status code: `204` (`NO_CONTENT`).

6. Delete user by ID.

Method: `DELETE`.

Endpoint: `/api/users/{id}`.

Access: `Admin`.

Path variable:

`id`.

Response:

`void`.

HTTP status code: `204` (`NO_CONTENT`).

</details>

### Products

<details>

<summary>View examples</summary>

Endpoints accessible by authenticated users.

1. Create product.

Method: `POST`.

Endpoint: `/api/products`.

Access: `Admin`.

Request body:

```json
{
    "title": "example",         // @NotBlank @Size(max = 75)
    "description": "example",   // @NotBlank @Size(max = 750)
    "price": 12.34              // @NotNull @Positive
}
```

Response:

```json
{
    "id": 123,
    "title": "example",
    "description": "example",
    "price": 12.34
}
```

2. Get product by ID.

Method: `GET`.

Endpoint: `/api/products/{id}`.

Access: `Admin, User`.

Response:

```json
{
    "id": 123,
    "title": "example",
    "description": "example",
    "price": 12.34
}
```

3. Get product by title.

Method: `GET`.

Endpoint: `/api/products?title=example`.

Access: `Admin, User`.

Request parameter:

`title`, `@NotBlank`.

Response:

```json
{
    "id": 123,
    "title": "example",
    "description": "example",
    "price": 12.34
}
```

4. Get products by description.

Method: `GET`.

Endpoint: `/api/products?description=example`.

Access: `Admin, User`.

Request parameter:

`description`, `@NotBlank`.

Response:

```json
[
    {
        "id": 123,
        "title": "example",
        "description": "example",
        "price": 12.34
    },
    {
        "id": 456,
        "title": "anotherExample",
        "description": "example",
        "price": 45.67
    }
]
```

5. Get all products.

Method: `GET`.

Endpoint: `/api/products`.

Access: `Admin, User`.

Response:

```json
[
    {
        "id": 123,
        "title": "example",
        "description": "example",
        "price": 12.34
    },
    {
        "id": 456,
        "title": "anotherExample",
        "description": "example",
        "price": 45.67
    }
]
```

6. Update product by ID.

Method: `PUT`.

Endpoint: `/api/products/{id}`.

Access: `Admin`.

Path variable:

`id`.

Request body:

```json
{
    "title": "exampleUpdated",          // @NotBlank @Size(max = 75)
    "description": "exampleUpdated",    // @NotBlank @Size(max = 750)
    "price": 11.22                      // @NotNull @Positive
}
```

Response:

```json
{
    "id": 123,
    "title": "exampleUpdated",
    "description": "exampleUpdated",
    "price": 11.22
}
```

7. Delete product by ID.

Method: `DELETE`.

Endpoint: `/api/products/{id}`.

Access: `Admin`.

Path variable:

`id`.

Response:

`void`.

HTTP status code: `204` (`NO_CONTENT`).

</details>

## Setup

### Generate an RSA Key Pair

```bash
mkdir -p src/main/resources/certs
cd src/main/resources/certs
openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
rm keypair.pem
```

Refer to [Securing REST APIs in Spring Boot 4 with JWT](https://ajinkya-mundankar.medium.com/securing-rest-apis-in-spring-boot-4-with-jwt-the-2026-way-no-custom-filters-5d36ab500e81) # Step 2 - Generate an RSA key pair.

## Run the Application

Run the application with Maven:

```bash
./mvnw spring-boot:run
```

## Build the Application

Start the MySQL container:

```bash
docker compose up -d
```

Build the JAR file:

```bash
./mvnw clean package
```

Run the JAR file:

```bash
java -jar target/jwt-secured-rest-api-0.0.1-SNAPSHOT.jar
```

## Docker and MySQL

Run the MySQL CLI inside the Docker container:

```bash
docker compose exec mysql mysql -u root -p
```

You will need to provide MySQL root password defined in [compose.yaml](compose.yaml).

## What You'd Change For Production

- Load keys from a secrets manager, not the classpath
- Add refresh tokens
- Configure CORS if your frontend is on a different origin

Refer to [Securing REST APIs in Spring Boot 4 with JWT](https://ajinkya-mundankar.medium.com/securing-rest-apis-in-spring-boot-4-with-jwt-the-2026-way-no-custom-filters-5d36ab500e81) # What you'd change for production.

## References

- [HTTP status codes reference](https://http.cat/)
- [Conventional Commits](https://www.conventionalcommits.org/)

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

### Documentation

[OAuth 2.0 Resource Server JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html).

### Tools

- [Spring Initializr](https://start.spring.io/)
