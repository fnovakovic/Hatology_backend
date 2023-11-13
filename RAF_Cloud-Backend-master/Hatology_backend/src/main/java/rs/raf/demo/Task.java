package rs.raf.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.Orders;
import rs.raf.demo.model.Product;
import rs.raf.demo.services.OrdersService;
import rs.raf.demo.services.ProductService;
import rs.raf.demo.utils.JwtUtil;

import java.util.List;

@Component
public class Task implements Runnable {


    private final JwtUtil jwtUtil;
    private final OrdersService ordersService;

    @Autowired
    public Task(JwtUtil jwtUtil, OrdersService ordersService) {

        this.jwtUtil = jwtUtil;
        this.ordersService = ordersService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Sleep for 10 minutes
                Thread.sleep(1 * 60 * 1000); // 10 minutes in milliseconds

                // Perform the counting task
                countNumbers();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void countNumbers() {
        List<Orders> orders =  ordersService.findAllOrders();

        for (Orders o:orders) {
            if(o.getUniqueId() != null && !o.getUniqueId().equals("")) {
                if (jwtUtil.isTokenExpiredd(o.getUniqueId()) == true) {
                    ordersService.deleteById(o.getUserId());

                }
            }
        }

    }


}


