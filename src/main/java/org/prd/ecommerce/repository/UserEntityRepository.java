package org.prd.ecommerce.repository;

import org.prd.ecommerce.entities.UserEntity;
import org.prd.ecommerce.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByEmail(String username);
    UserEntity findByUserRole (UserRole role);
}
