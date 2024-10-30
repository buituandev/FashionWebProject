package walkonmoon.fashion.model;

public class CartItemId {
    private int productId;
    private int userId;

    public CartItemId() {
    }

    public CartItemId(int productId, int userId) {
        this.productId = productId;
        this.userId = userId;
    }
}
