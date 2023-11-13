package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private Long id;

    private String category;

    private String name;

    private String description;

    private String price;

    private String previousPrice;

    private String checkedNew;

}
