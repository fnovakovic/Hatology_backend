package rs.raf.demo.repositories;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Contact;
import rs.raf.demo.model.Orders;
import rs.raf.demo.model.Product;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface ContactCrudRepository extends CrudRepository<Contact, Long> {

    @Transactional
    @Modifying
    @Query("delete from Contact WHERE userId = :userId")
    void delete(@Param("userId") Long userId);



}
