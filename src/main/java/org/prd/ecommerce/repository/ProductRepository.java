package org.prd.ecommerce.repository;

import jakarta.persistence.NamedNativeQuery;
import org.prd.ecommerce.entities.dto.ProductAdminView;
import org.prd.ecommerce.entities.dto.ProductCustomerView;
import org.prd.ecommerce.entities.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByNameContaining(String name);
    Page<Product> findByNameContaining(String name, Pageable pageable);
    /*
    * private Long id;
    private String name;
    private Long price;
    private String principalUrl;
    private Date createdAt;
    private boolean enabled;
    private String categoryName;*/
    @Query("SELECT new org.prd.ecommerce.entities.dto.ProductAdminView(p.id, p.name, p.price, p.principalUrl, p.createdAt, p.enabled, p.category.name) FROM Product p")
    Page<ProductAdminView> findSimpleProductsViewInformation(Pageable pageable);
//    @Query("SELECT new Product(p.id, p.name, p.price, p.principalUrl, p.createdAt, p.enabled, p.category) FROM Product p")
//    Page<Product> findSimpleProductsInformation(Pageable pageable);
    @Query("SELECT new org.prd.ecommerce.entities.dto.ProductAdminView(p.id, p.name, p.price, p.principalUrl, p.createdAt, p.enabled, p.category.name) FROM Product p WHERE p.name LIKE %?1%")
    Page<ProductAdminView> findSimpleProductsViewInformationByNameContaining(String name, Pageable pageable);
    boolean existsByName(String name);

    //traer los 8 primeros productos ordenados por fecha de creacion mas reciente
//    @Query("SELECT new org.prd.ecommerce.entities.dto.ProductCustomerView(p.id, p.name, p.price, p.category.name, p.principalUrl) FROM Product p ORDER BY p.createdAt DESC limit 8")
//    List<ProductCustomerView> findTop8ByOrderByCreatedAtDesc();
    //get promotion whit start date is less than or equal to current date and end date is greater than or equal to current date

    @Query("select DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE pr.enabled = true OR pr IS NULL ORDER BY p.createdAt DESC limit 8")
    List<Product> findTop8ByOrderByCreatedAtDesc();

    //traer los 8 primeros productos ordenados por promotion de mayor descuento en porcentage
    @Query("select distinct p from Product p left join p.promotions pr where pr.enabled = true order by pr.discount desc limit 8")
    List<Product> findTop8ByOrderByPromotionsDiscountDesc();

    //traer los products con la promotion que este enabled true en lista
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE pr.enabled = true OR pr IS NULL")
    Page<Product> findAllProductsWithPromotionsOrWithout(Pageable pageable);
    //traer los products con la promotion que este enabled true en lista filtrados por name
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL) AND p.name LIKE %?1%")
    Page<Product> findAllProductsWithPromotionsOrWithoutByNameContaining(String name, Pageable pageable);





//selecionar products por varias categorias y traer las promotions que esten enabled true
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL) AND p.category.id IN :categoriesIds")
    Page<Product> findAllByCategoriesIds(@Param("categoriesIds") List<Long> categoriesIds, Pageable pageable);
//selecionar products por varias categorias y traer las promotions que esten enabled true filtrados por name
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL) AND p.category.id IN :categoriesIds AND p.name LIKE %:name%")
    Page<Product> findAllByCategoriesIdsAndNameContaining(@Param("categoriesIds") List<Long> categoriesIds, @Param("name") String name, Pageable pageable);
    //selecionar products por varias categorias y traer las promotions que esten enabled true filtrados por name y precio menor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL) AND p.category.id IN :categoriesIds AND p.name LIKE %:name% AND p.price <= :menorDe")
    Page<Product> findAllByCategoriesIdsAndNameContainingAndPriceLessThanEqual(@Param("categoriesIds") List<Long> categoriesIds, @Param("name")String name, @Param("menorDe")Double menorDe, Pageable pageable);
    //selecionar products por varias categorias y traer las promotions que esten enabled true filtrados por name y precio mayor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL) AND p.category.id IN :categoriesIds AND p.name LIKE %:name% AND p.price >= :mayorDe")
    Page<Product> findAllByCategoriesIdsAndNameContainingAndPriceGreaterThanEqual(@Param("categoriesIds") List<Long> categoriesIds, @Param("name")String name, @Param("mayorDe")Double mayorDe, Pageable pageable);
    //selecionar products por varias categorias y traer las promotions que esten enabled true filtrados por name y precio mayor o igual a y menor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL) AND p.category.id IN :categoriesIds AND p.name LIKE %:name% AND p.price >= :mayorDe AND p.price <= :menorDe")
    Page<Product> findAllByCategoriesIdsAndNameContainingAndPriceGreaterThanEqualAndPriceLessThanEqual(@Param("categoriesIds") List<Long> categoriesIds, @Param("name")String name, @Param("mayorDe")Double mayorDe, @Param("menorDe")Double menorDe, Pageable pageable);

    //seleccionar productos por varias categorias y traer las promotions que esten enabled true y precio mayor o menor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL) AND p.category.id IN :categoriesIds AND p.price >= :mayorDe AND p.price <= :menorDe")
    Page<Product> findAllByCategoriesIdsAndPriceGreaterThanEqualAndPriceLessThanEqual(@Param("categoriesIds") List<Long> categoriesIds, @Param("mayorDe")Double mayorDe, @Param("menorDe")Double menorDe, Pageable pageable);
    //seleccionar productos por varias categorias y traer las promotions que esten enabled true y precio menor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL) AND p.category.id IN :categoriesIds AND p.price <= :menorDe")
    Page<Product> findAllByCategoriesIdsAndPriceLessThanEqual(@Param("categoriesIds") List<Long> categoriesIds, @Param("menorDe") Double menorDe, Pageable pageable);
    //seleccionar productos por varias categorias y traer las promotions que esten enabled true y precio mayor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL) AND p.category.id IN :categoriesIds AND p.price >= :mayorDe")
    Page<Product> findAllByCategoriesIdsAndPriceGreaterThanEqual(@Param("categoriesIds") List<Long> categoriesIds, @Param("mayorDe") Double mayorDe, Pageable pageable);

    //seleccionar productos por solo precio menor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL ) AND p.price <= :menorDe")
    Page<Product> findAllByPriceLessThanEqual(@Param("menorDe")Double menorDe, Pageable pageable);
    //seleccionar productos por solo precio mayor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL ) AND p.price >= :mayorDe")
    Page<Product> findAllByPriceGreaterThanEqual(@Param("mayorDe")Double mayorDe, Pageable pageable);
    //seleccionar productos por solo precio mayor o igual a y menor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL ) AND p.price >= :mayorDe AND p.price <= :menorDe")
    Page<Product> findAllByPriceGreaterThanEqualAndPriceLessThanEqual(@Param("mayorDe")Double mayorDe, @Param("menorDe")Double menorDe, Pageable pageable);

    //seleccionar productos por solo name y traer las promotions que esten enabled true y precio menor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL ) AND p.name LIKE %:name% AND p.price <= :menorDe")
    Page<Product> findAllByNameContainingAndPriceLessThanEqual(@Param("name") String name, @Param("menorDe")Double menorDe, Pageable pageable);
    //seleccionar productos por solo name y traer las promotions que esten enabled true y precio mayor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL ) AND p.name LIKE %:name% AND p.price >= :mayorDe")
    Page<Product> findAllByNameContainingAndPriceGreaterThanEqual(@Param("name")String name, @Param("mayorDe")Double mayorDe, Pageable pageable);
    //seleccionar productos por solo name y traer las promotions que esten enabled true y precio mayor o igual a y menor o igual a
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.promotions pr WHERE (pr.enabled = true OR pr IS NULL ) AND p.name LIKE %:name% AND p.price >= :mayorDe AND p.price <= :menorDe")
    Page<Product> findAllByNameContainingAndPriceGreaterThanEqualAndPriceLessThanEqual(@Param("name")String name, @Param("mayorDe")Double mayorDe, @Param("menorDe")Double menorDe, Pageable pageable);




    @Modifying
    @Query("UPDATE Product p SET p.enabled = :status WHERE p.id = :id")
    void changeEnabledStatus(@Param("id") Long id, @Param("status") boolean status);

    Integer countByCategory_Id(Long id);

}
