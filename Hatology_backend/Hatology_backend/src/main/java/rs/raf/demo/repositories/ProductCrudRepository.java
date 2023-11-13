package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Orders;
import rs.raf.demo.model.Product;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface ProductCrudRepository extends CrudRepository<Product, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Product SET name = :name, description = :description, price = :price, checkedNew = :checkedNew, category = :category WHERE  Id = :Id")
    void update(@Param("name") String name,@Param("description") String description,@Param("price") String price,@Param("checkedNew") String checkedNew,@Param("category") String category, @Param("Id") Long Id);

}
