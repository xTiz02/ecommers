package org.prd.ecommerce.repository;

import org.prd.ecommerce.entities.dto.CategoryCustomerView;
import org.prd.ecommerce.entities.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);
    Optional<Category> findById(Long id);

    Page<Category> findByNameContaining(String name, Pageable pageable);
    @Query("SELECT new org.prd.ecommerce.entities.dto.CategoryCustomerView(c.id, c.name, c.imgUrl) FROM Category c")
    List<CategoryCustomerView> getCategoriesToCustomerView();

    @Query("select new org.prd.ecommerce.entities.dto.CategoryCustomerView(c.id,c.name) from Category c")
    List<CategoryCustomerView> getSimpleCategoriesToCustomerView();
    //get publicId by id
    @Query("SELECT c.publicId FROM Category c WHERE c.id = ?1")
    String getPublicIdById (Long id);

    int countByName(String name);
}
