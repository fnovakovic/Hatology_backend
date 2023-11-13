package rs.raf.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.*;
import rs.raf.demo.services.*;

import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersService ordersService;
    private final ProductService productService;
    private final EmailService emailService;
    private UserService userService;
    private User currUser;


    public OrdersController(OrdersService ordersService, ProductService productService, EmailService emailService, UserService userService){
        this.ordersService = ordersService;
        this.productService = productService;
        this.emailService = emailService;
        this.userService = userService;
    }


    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Orders>> getAllOrders(){
        currUser =   userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getAdminPermission().equals("1")){
            List<Orders> orders = ordersService.findAllOrders();
            return ResponseEntity.ok(orders);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping(value = "/delete",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deliver(@RequestBody OrdDto orders){

        currUser =  userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getAdminPermission().equals("1")){

            ordersService.deleteOrder(orders.getUserId());

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }



    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createOrder(@RequestBody OrdersDto orders){

            Orders o = new Orders();
            o = ordersService.findOrderbyUniqueId(orders.getUniqueId());
            o.setUniqueId("");
            o.setFirstName(orders.getFirstName());
            o.setLastName(orders.getLastName());
            o.setAdress(orders.getAdress());
            o.setEmail(orders.getEmail());
            o.setNumber(orders.getNumber());
            o.setDelivered("NO");
            long sum = 0;
            for (Product product: o.getProducts()) {
                sum = sum +  Long.valueOf(product.getPrice());
            }

            o.setNeedToPay(String.valueOf(sum));
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
            Date date = new Date();
            String formattedDate = formatter.format(date);
            o.setDateTime(formattedDate);

            ordersService.save(o);
            emailService.sendEmail(orders.getEmail(),"ODGOVOR","NEKI TEXT");

            return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping(value = "/buy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity buy(@RequestBody BuyDto buyDto) {
        Orders ord = ordersService.findOrderbyUniqueId(buyDto.getUniqueId());
        Product p = productService.findById(buyDto.getProductId()).orElse(null);

        if (ord != null) {
            Collection<Product> productCollection = ord.getProducts();

            productCollection.add(p);

            ordersService.updateProductsInOrder(productCollection, ord.getUniqueId());

            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            Orders newOrder = new Orders();
            Collection<Product> productCollection = new ArrayList<>();
            productCollection.add(p);

            newOrder.setProducts(productCollection);
            newOrder.setUniqueId(buyDto.getUniqueId());

            ordersService.save(newOrder);

            return ResponseEntity.ok(HttpStatus.OK);
        }

    }

    @PostMapping(value = "/ad", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity ad(@RequestBody BuyDto buyDto){
        Orders ord = ordersService.findOrderbyUniqueId(buyDto.getUniqueId());
        Product p = productService.findById(buyDto.getProductId()).orElse(null);

            Collection<Orders> ordersCollection = p.getOrders();

            ordersCollection.add(ord);
            productService.updateOrdersInProduct(ordersCollection, p.getId());
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping(value = "/remove", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity remove(@RequestBody BuyDto buyDto) {
        Orders ord = ordersService.findOrderbyUniqueId(buyDto.getUniqueId());
        Product p = productService.findById( buyDto.getProductId()).orElse(null);


        Collection<Product> pp = ord.getProducts();
        for (Product a: pp) {
            if(a.getId().equals(p.getId())){
                pp.remove(a);
                break;
            }
        }
        ordersService.updateProductsInOrder(pp, ord.getUniqueId());

            return ResponseEntity.ok(HttpStatus.OK);

    }



    @PostMapping(value = "/gett", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Orders> gett(@RequestBody BuyDto buyDto) {
        return ResponseEntity.ok(ordersService.findOrderbyUniqueId(buyDto.getUniqueId()));
    }


    @PostMapping(value = "/removeOrder", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeOrder(@RequestBody OrderDto2 ordersDto) {
        currUser =  userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getAdminPermission().equals("1")) {
            ordersService.deleteById(ordersDto.getId());
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping(value = "/updatePrice",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePrice(@RequestBody ProductDto productDto){
        currUser =  userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getAdminPermission().equals("1")) {
        Long sum = Long.valueOf(0);
        boolean flag = false;
        Product p = productService.findById(productDto.getId()).orElse(null);

        List<Orders> orders = ordersService.findAllOrders();

        for (Orders ord: orders) {
            for (Product pr:ord.getProducts()) {
                 if(pr.getId() == p.getId()){
                     Long currentNeedToPay = Long.parseLong(ord.getNeedToPay());
                       sum = currentNeedToPay;
                       sum = sum - Long.valueOf(p.getPrice());
                       sum = sum + Long.valueOf(productDto.getPrice());
                       flag = true;
                 }
            }
            if(flag == true) {
                flag = false;
                ordersService.updatePricee(String.valueOf(sum), ord.getUserId());
                sum = Long.valueOf(0);
            }
        }


//        emailService.sendEmail(orders.getEmail(),"ODGOVOR","NEKI TEXT");

            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.status(403).build();
    }



}
