package walkonmoon.fashion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Data;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "product_name")
    public String product_name;
    // Rename category_id to categoryId
    @Column(name = "category_id")
    private int categoryId; // Changed here
    @Column(name = "stock")
    private int stock;
    @Column(name = "short_description")
    private String short_description;
    @Column(name = "updated_date")
    private Date update_date;
    @Column(name = "SKU")
    private String SKU;
    @Column(name = "long_description")
    private String long_description;
    @Column(name = "image_collection_url")
    private String image_collection_url;
    @Column(name = "price")
    private int price;
    @Getter
    @Column(name = "is_trend")
    private Boolean isTrend = false;
    @Column(name = "is_new")
    private Boolean isNew = false;
    @Column(name = "status")
    private String status;

}
