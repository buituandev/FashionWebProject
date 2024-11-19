package walkonmoon.fashion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "blog_detail")
public class BlogDetail {
    @Id
    private int id;

    @Column(name = "blog_id")
    private int blogID;

    @Column(name = "content", length = 1000000)
    private String content;
}