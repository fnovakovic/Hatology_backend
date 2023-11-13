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
public interface OrdersCrudRepositoru extends CrudRepository<Orders, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE orders SET need_to_pay = CAST(CAST(need_to_pay AS INTEGER) - CAST(:needToPay AS INTEGER) AS CHAR) WHERE user_id = :userId", nativeQuery = true)
    void updatePrice(@Param("needToPay") String needToPay, @Param("userId") Long userId);


    @Transactional
    @Modifying
    @Query(value = "UPDATE orders SET need_to_pay = :needToPay WHERE user_id = :userId", nativeQuery = true)
    void updatePricee(@Param("needToPay") String needToPay, @Param("userId") Long userId);

}
