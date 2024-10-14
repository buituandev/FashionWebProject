package walkonmoon.fashion.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;
@Getter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String product_name;
    @Column
    private int categoryId;
    @Column
    private int stock;
    @Column
    private String title;
    @Column
    private String shortDescription;
    @Column
    private Date updateDate;
    @Column
    private String SKU;

    public int getId() {
        return id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getStock() {
        return stock;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getSKU() {
        return SKU;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public int getImageCollectionId() {
        return ImageCollectionId;
    }

    @Column
    private String longDescription;
    @Column
    private int ImageCollectionId;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.product_name = name;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public void setImageCollectionId(int imageCollectionId) {
        ImageCollectionId = imageCollectionId;
    }
}
