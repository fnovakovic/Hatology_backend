package rs.raf.demo.controllers;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.raf.demo.model.*;

import rs.raf.demo.services.OrdersService;
import rs.raf.demo.services.ProductService;
import rs.raf.demo.services.UserService;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final OrdersService ordersService;

    private final SocketIOServer socketServer;
    private UserService userService;
    private User currUser;



    public ProductController(ProductService productService, OrdersService ordersService, SocketIOServer socketServer, UserService userService) {
        this.productService = productService;
        this.ordersService = ordersService;
        this.socketServer = socketServer;

        this.userService = userService;
    }


    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);

    }

    @PostMapping(value = "/getAllByCategory",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getAllByCategory(@RequestBody ProductDto productDto){
        List<Product> products = productService.getAllByCategory(productDto.getCategory());
        return ResponseEntity.ok(products);
    }


    @PostMapping("/upload")
   public ResponseEntity<Product> handleFileUpload(@RequestParam("files[]") MultipartFile[] files,@RequestParam("name") String name,@RequestParam("description") String description,@RequestParam("price") String price,@RequestParam("category") String category,@RequestParam("checkedNew") String checkedNew) throws IOException {

        String tmp = "";

        currUser = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (currUser.getAdminPermission().equals("1")) {

            Product productt = new Product();
            productt.setName(name);
            productt.setDescription(description);
            productt.setPrice(price);
            productt.setCategory(category);
            productt.setCheckedNew(checkedNew);

            for (MultipartFile file : files) {




                String uploadDir = "C:\\Users\\filip\\Desktop\\hatology_front\\public";
                String uploadDir2 = "public/";

                String fileName2 = file.getOriginalFilename();

                String filePath = Paths.get(uploadDir, fileName2).toString();

                String filePath2 = uploadDir2 + fileName2 + ",";
                tmp = tmp + filePath2;
                Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            }
            productt.setImageData(tmp);
            productService.save(productt);

            socketServer.getBroadcastOperations().sendEvent("newProductAdded", productt);

            return ResponseEntity.ok(productt);

        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping(value = "/removeProduct", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeProduct(@RequestBody ProductDto productDto) {
        currUser = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (currUser.getAdminPermission().equals("1")) {

            Product product = productService.findById(productDto.getId()).orElse(null);
            List<Orders> o = ordersService.findAllOrders();
            for (Orders or:o) {
               ordersService.updatePrice(product.getPrice(),or.getUserId());
            }
            if (product != null) {
                productService.removeProductAndAssociatedOrders(product);
                return ResponseEntity.ok(HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }

        }
        return ResponseEntity.status(403).build();
    }


    @PostMapping(value = "/updateProduct",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateProduct(@RequestBody ProductDto productDto){
        currUser = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            if (currUser.getAdminPermission().equals("1")) {
                productService.update(productDto);

                return ResponseEntity.ok(HttpStatus.OK);
            }
        return ResponseEntity.status(403).build();
    }



}
