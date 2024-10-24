package walkonmoon.fashion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
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
    @Column(name = "title")
    private String title;
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
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", product_name='" + product_name + '\'' +
                ", category_id=" + categoryId +
                ", stock=" + stock +
                ", title='" + title + '\'' +
                ", short_description='" + short_description + '\'' +
                ", update_date=" + update_date +
                ", SKU='" + SKU + '\'' +
                ", long_description='" + long_description + '\'' +
                ", image_collection_id=" + image_collection_url +
                ", price=" + price +
                '}';
    }
}
