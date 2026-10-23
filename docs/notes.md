# Notes

## JPA

```java
@ElementCollection(fetch = FetchType.EAGER)
@CollectionTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id")
)
@Enumerated(EnumType.STRING)
@Column(name = "role", nullable = false)
private Set<Role> roles = new HashSet<>();
```

- `@ElementCollection`: stores the collection in a separate table because one user can have multiple roles
    - `fetch = FetchType.EAGER`: loads roles whenever the user is loaded
- `@CollectionTable`: configures the table storing the roles
    - `name = "user_roles"`: names that table `user_roles`
    - `@JoinColumn(name = "user_id")`: links each role to its user's primary key
- `@Enumerated(EnumType.STRING)`: stores enum names (`USER`, `ADMIN`) as strings instead of numbers
- `@Column(name = "role", nullable = false)`: names the column role and disallows NULL
