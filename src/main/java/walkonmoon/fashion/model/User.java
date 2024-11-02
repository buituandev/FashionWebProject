package walkonmoon.fashion.model;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
    @Column(nullable = false)
    private String password;
    @Column
    private String gender;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private Date dob;
    @Column(nullable = true)
    private String address;
    @Column
    private String phone_number;
    @Column(nullable = true)
    private String image;
    @Column(nullable = true)
    private int type;
    @Column
    private int is_deleted;
    @Column(nullable = true)
    private String province;
    @Column
    private String email;


}
