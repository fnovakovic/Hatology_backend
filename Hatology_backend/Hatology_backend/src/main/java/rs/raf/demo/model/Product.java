package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.criterion.Order;
import org.springframework.web.multipart.MultipartFile;

import javax.lang.model.type.ArrayType;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    @Column
    private String name;

    @Column
    @Lob
    private String imageData;

    @NotBlank
    @Column
    private String description;

    @NotBlank
    @Column
    private String price;

    @NotBlank
    @Column
    private String category;

    @NotBlank
    @Column
    private String checkedNew;

    @ManyToMany(mappedBy = "products",cascade = CascadeType.REMOVE)
    @JsonBackReference
    private Collection<Orders> orders = new ArrayList<>();


}
