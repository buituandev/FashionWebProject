package walkonmoon.fashion.model;

import java.io.Serializable;
import java.util.Objects;

public class CartItemId implements Serializable {
    private int productId;
    private int userId;

    public CartItemId() {
    }

    public CartItemId(int productId, int userId) {
        this.productId = productId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemId that = (CartItemId) o;
        return productId == that.productId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, userId);
    }
}