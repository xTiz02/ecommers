package org.prd.ecommerce.repository;

import org.prd.ecommerce.entities.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long>{
}
