package org.prd.ecommerce.repository;

import org.prd.ecommerce.entities.entity.UserEntity;
import org.prd.ecommerce.config.util.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByEmail(String username);
    UserEntity findByUserRole (UserRole role);
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
