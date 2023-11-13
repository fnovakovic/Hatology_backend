package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String uniqueId;


    @Column
    private String firstName;


    @Column
    private String lastName;


    @Column
    private String email;


    @Column
    private String number;


    @Column
    private String adress;


    @Column
    private String dateTime;


    @Column
    private String needToPay;

    @Column
    private String delivered;

    @ManyToMany
    @JsonManagedReference
    private Collection<Product> products = new ArrayList<>();


}
