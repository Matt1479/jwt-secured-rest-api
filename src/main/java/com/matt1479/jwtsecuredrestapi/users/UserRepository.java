package com.matt1479.jwtsecuredrestapi.users;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    
    Optional<User> findByUsername(String name);

    boolean existsByUsername(String username);

}
