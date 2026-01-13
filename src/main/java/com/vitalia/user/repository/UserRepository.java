package com.vitalia.user.repository;

import com.vitalia.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for User entity
 * TODO: Add findByUsername method
 * TODO: Add findByEmail method
 * TODO: Add existsByUsername method
 * TODO: Add existsByEmail method
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // TODO: Implement custom query methods
    
}
