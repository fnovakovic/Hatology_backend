package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Orders;
import rs.raf.demo.model.Product;
import rs.raf.demo.model.ProductDto;
import rs.raf.demo.repositories.ProductCrudRepository;
import rs.raf.demo.repositories.ProductRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private ProductCrudRepository productCrudRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,ProductCrudRepository productCrudRepository) {
        this.productRepository = productRepository;
        this.productCrudRepository = productCrudRepository;

    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> getAllByCategory(String category) {
        return productRepository.findAllByCategory(category);
    }



    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    public void update(ProductDto productDto) {

        productCrudRepository.update(productDto.getName(), productDto.getDescription(), productDto.getPrice(), productDto.getCheckedNew(), productDto.getCategory(), productDto.getId());
    }

    @Transactional
    public void updateOrdersInProduct(Collection<Orders> orders, Long uniqueId) {
        Product p = productRepository.findById(uniqueId).orElse(null);
        if (p != null) {
            p.setOrders(orders);
            productRepository.save(p);
        }
    }


    @Transactional
    public void removeProductAndAssociatedOrders(Product product) {
        Collection<Orders> associatedOrders = product.getOrders();

        for (Orders order : associatedOrders) {
            order.getProducts().remove(product);
        }

        product.getOrders().clear();
        productRepository.delete(product);
    }

}
