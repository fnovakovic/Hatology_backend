package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class OrdersDto {
    private Long UserId;

    private String uniqueId;


    private String firstName;



    private String lastName;


    private String email;


    private String number;


    private String adress;





}
