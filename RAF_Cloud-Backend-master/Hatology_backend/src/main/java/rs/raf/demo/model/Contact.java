package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Column
    private String name;

    @NotBlank
    @Column
    private String email;

    @NotBlank
    @Column
    private String comment;
}
