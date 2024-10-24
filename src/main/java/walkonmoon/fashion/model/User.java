package walkonmoon.fashion.model;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String full_name;
    @Column
    private String password;
    @Column
    private String gender;
    @Column
    private Date dob;
    @Column
    private String address;
    @Column
    private String phone_number;
    @Column
    private String image;
    @Column
    private int type;
    @Column
    private int is_deleted;
    @Column
    private String province;
    @Column
    private String email;


}
