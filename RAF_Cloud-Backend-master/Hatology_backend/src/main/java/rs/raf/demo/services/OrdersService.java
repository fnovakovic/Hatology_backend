package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Orders;
import rs.raf.demo.model.Product;
import rs.raf.demo.repositories.*;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class OrdersService {
    private OrdersRepository ordersRepository;
    private OrdersCrudRepositoru ordersCrudRepositoru;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository, OrdersCrudRepositoru ordersCrudRepositoru) {
        this.ordersRepository = ordersRepository;
        this.ordersCrudRepositoru = ordersCrudRepositoru;

    }

    public List<Orders> findAllOrders() {
        return ordersRepository.findAll();
    }

    public void deleteOrder(Long userId) {
         ordersCrudRepositoru.deleteById(userId);
    }
    public Orders findOrderbyUniqueId(String uniqueId) {
        return ordersRepository.findOrdersByUniqueId(uniqueId);
    }



    public Orders save(Orders order) {
        return ordersRepository.save(order);
    }

    @Transactional
    public void updateProductsInOrder(Collection<Product> products, String uniqueId) {
        Orders order = ordersRepository.findOrdersByUniqueId(uniqueId);
        if (order != null) {
            order.setProducts(products);
            ordersRepository.save(order);
        }
    }


    public void deleteById(Long id) {
        ordersRepository.deleteById(id);
    }

    public void updatePrice(String price,Long id) {
        ordersCrudRepositoru.updatePrice(price,id);
    }
    public void updatePricee(String price,Long id) {
        ordersCrudRepositoru.updatePricee(price,id);
    }


}
