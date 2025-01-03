package walkonmoon.fashion.model;
import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
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
    @Column(name = "address", length = 1000000, nullable = true)
    private String address;
    @Column
    private String phone_number;
    @Column(nullable = true)
    private String image;
    @Column(nullable = true)
    private UserType type;
//    @Column
//    private int is_deleted;
//    @Column(name = "province", nullable = true)
//    private String province;
    @Column
    private String email;
    @Column(name = "status")
    private UserStatus status = UserStatus.ACTIVE;
    @Column(nullable = true)
    private String token;
    @Column(name = "token_expired", nullable = true)
    private LocalDateTime tokenExpired;

}