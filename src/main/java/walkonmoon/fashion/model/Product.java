package walkonmoon.fashion.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="product_name")
    public String product_name;
    @Column(name="category_id")
    private int category_id;
    @Column(name="stock")
    private int stock;
    @Column(name="title")
    private String title;
    @Column(name="short_description")
    private String short_description;
    @Column(name="updated_date")
    private Date update_date;
    @Column(name="SKU")
    private String SKU;
    @Column(name="long_description")
    private String long_description;
    @Column(name="image_collection_url")
    private String image_collection_url;

    public String getImage_collection_url() {
        return image_collection_url;
    }

    public void setImage_collection_url(String image_collection_url) {
        this.image_collection_url = image_collection_url;
    }

    @Column(name="image_collection_url")
    private String image_collection_url;

    @Column(name = "price")
    private int price;

    public void setId(int id) {
        this.id = id;
    }

    public void setProduct_name(String name) {
        this.product_name = name;
    }

    public void setCategory_id(int categoryId) {
        this.category_id = categoryId;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShort_description(String shortDescription) {
        this.short_description = shortDescription;
    }

    public void setUpdate_date(Date updateDate) {
        this.update_date = updateDate;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public void setLong_description(String longDescription) {
        this.long_description = longDescription;
    }

    public void setImage_collection_url(String image_collection_url) {
        this.image_collection_url = image_collection_url;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public String getProduct_name() {
        return product_name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getId() {
        return id;
    }

    public int getStock() {
        return stock;
    }

    public String getTitle() {
        return title;
    }

    public String getShort_description() {
        return short_description;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public String getSKU() {
        return SKU;
    }

    public String getLong_description() {
        return long_description;
    }

    public String getImage_collection_url() {
        return image_collection_url;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", product_name='" + product_name + '\'' +
                ", category_id=" + category_id +
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
