document.addEventListener('DOMContentLoaded', function () {
    const cartService = new CartService();

    document.querySelectorAll('.btn-cart').forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault();
            const productElement = this.closest('.product-layout');
            const product = {
                id: productElement.querySelector('.product-id').textContent,
                title: productElement.querySelector('.product-name').innerText,
                image: productElement.querySelector('img').src,
                price: productElement.querySelector('.product-price-hid').textContent,
                stock: productElement.querySelector('.product-stock').textContent
            };
            console.log(product);
            console.log(productElement);
            cartService.addToCart(product, 1);
        });
    });

    updateCartDisplay();
});

document.addEventListener('DOMContentLoaded', function () {
    const cartService = new CartService();
    document.querySelectorAll('.cart-btn').forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault();
            const productElement = this.closest('.single-product-wrapper');
            const product = {
                id: productElement.querySelector('.product-id').textContent,
                title: productElement.querySelector('.product-name-hid').textContent,
                image: productElement.querySelector('.product-img').src,
                price: productElement.querySelector('.product-price-hid').textContent,
                stock: productElement.querySelector('.product-stock').textContent
            };
            const quantity = productElement.querySelector('.cart-input-box').value;
            console.log(product);
            console.log(productElement);
            cartService.addToCart(product, quantity);
        });
    });

    updateCartDisplay();
});

function addItemToCart(item, quant = 1, stock) {
    const cart = loadCart();
    const existingItem = cart.find(cartItem => cartItem.id === item.id);
    const quantity = parseInt(quant); // Ensure quant is treated as a number

    if (quantity > parseInt(stock)) {
        alert("Số lượng sản phẩm trong giỏ hàng vượt quá số lượng sản phẩm trong kho");
        return;
    }
    if (existingItem) {
        existingItem.stock = stock;
        existingItem.quantity += quantity;
        if (existingItem.quantity > parseInt(stock)) {
            existingItem.quantity = parseInt(stock);
        }
    } else {
        item.quantity = quantity;
        cart.push(item);
    }
    saveCart(cart);
    updateCartDisplay();
}

function saveCart(cart) {
    localStorage.setItem('cart', JSON.stringify(cart));
}

function loadCart() {
    const cart = localStorage.getItem('cart');
    return cart ? JSON.parse(cart) : [];
}

function updateCartDisplay() {
    const cart = loadCart();
    const cartContent = document.querySelector('.shopping-cart-content ul');
    const cartTotal = document.querySelector('.shopping-cart-total');
    const cartButtons = document.querySelector('.shopping-cart-btn');
    const countStyle = document.querySelector('.count-style');

    if (!cartContent || !cartTotal || !cartButtons || !countStyle) {
        console.error('One or more cart elements are missing in the DOM.');
        return;
    }

    cartContent.innerHTML = '';
    if (cart.length === 0) {
        const emptyCartItem = document.createElement('li');
        emptyCartItem.className = 'cart-tempty-title';
        emptyCartItem.style.display = 'block';
        emptyCartItem.innerHTML = '<h3>Your cart is currently empty.</h3>';
        cartContent.appendChild(emptyCartItem);

        cartTotal.style.display = 'none';
        cartButtons.style.display = 'none';
        countStyle.innerText = '0';
    } else {
        cartTotal.style.display = 'block';
        cartButtons.style.display = 'block';
        countStyle.innerText = cart.length;

        let total = 0;
        cart.forEach(item => {
            total += parseFloat(item.price.replace('$', '')) * item.quantity;
            const cartItem = document.createElement('li');
            cartItem.classList.add('single-cart-item', 'media');
            cartItem.innerHTML = `
                <div class="shopping-cart-img me-4">
                    <a href="#">
                        <img class="img-fluid" alt="Cart Item" src="${item.image}">
                        <span class="product-quantity">${item.quantity}</span>
                    </a>
                </div>
                <div class="shopping-cart-title flex-grow-1">
                    <h4><a href="#">${item.title}</a></h4>
                    <p class="cart-price">${parseFloat(item.price.replace('$', '')).toLocaleString('vi-VN', { minimumFractionDigits: 0, maximumFractionDigits: 0 })} VND</p>
                </div>
                <div class="shopping-cart-delete">
                    <a href="#" data-id="${item.id}"><i class="ion ion-md-close"></i></a>
                </div>
            `;
            cartContent.appendChild(cartItem);
        });

        cartTotal.querySelector('span').innerText = `${total.toLocaleString('vi-VN', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).replace(',00', '')} VND`;

        // Add event listeners to delete buttons
        document.querySelectorAll('.shopping-cart-delete a').forEach(button => {
            button.addEventListener('click', function (event) {
                event.preventDefault();
                const itemId = this.getAttribute('data-id');
                removeItemFromCart(itemId);
            });
        });
    }
}

function removeItemFromCart(itemId) {
    let cart = loadCart();
    cart = cart.filter(item => item.id !== itemId);
    saveCart(cart);
    updateCartDisplay();
}

class CartService {
    constructor() {
        this.LOCAL_STORAGE_KEY = 'cart';
        this.OFFLINE_OPERATION_KEY = 'offline-cart-operation';
    }

    isLoggedIn() {
        return localStorage.getItem('userId') !== null;
    }

    async addToCart(product, quantity = 1) {
    const stock = parseInt(product.stock);
    const quant = parseInt(quantity);

    if (quant > stock) {
        alert("Số lượng sản phẩm trong giỏ hàng vượt quá số lượng sản phẩm trong kho");
        return;
    }

    if (this.isLoggedIn()) {
        const userId = parseInt(localStorage.getItem('userId')); // Ensure userId is a number
        console.log(typeof userId);
        try {
            const response = await fetch('/merge', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: userId,
                    localCartItems: [{ ...product, quantity: quant }]
                })
            });
            if (!response.ok) {
                throw new Error('Failed to add item to cart');
            }
        } catch (error) {
            console.error('Error adding item to cart:', error);
            //this.queueOfflineOperation({ type: 'add', product, quantity: quant });
            //Warning
            alert('Không thể thêm sản phẩm vào giỏ hàng');
        }
    }
    this.addToLocalStorageCart(product, quant);
}

addToLocalStorageCart(product, quantity) {
    const cart = this.loadCart();
    const existingItem = cart.find(item => item.id === product.id);
    const stock = parseInt(product.stock);

    if (existingItem) {
        existingItem.stock = stock;
        existingItem.quantity += quantity;
        if (existingItem.quantity > stock) {
            existingItem.quantity = stock;
        }
    } else {
        product.quantity = quantity;
        cart.push(product);
    }
    this.saveCart(cart);
    updateCartDisplay();
}

saveCart(cart) {
    localStorage.setItem(this.LOCAL_STORAGE_KEY, JSON.stringify(cart));
}

    queueOfflineOperation(operation) {
        const operations = JSON.parse(localStorage.getItem(this.OFFLINE_OPERATION_KEY)) || [];
        operations.push(operation);
        localStorage.setItem(this.OFFLINE_OPERATION_KEY, JSON.stringify(operations));
    }

    async syncOfflineOperations() {
        if (this.isLoggedIn()) {
            const operations = JSON.parse(localStorage.getItem(this.OFFLINE_OPERATION_KEY)) || [];
            for (const operation of operations) {
                if (operation.type === 'add') {
                    await this.addToCart(operation.product, operation.quantity);
                }
            }
            localStorage.removeItem(this.OFFLINE_OPERATION_KEY);
        }
    }

    loadCart() {
        const cart = localStorage.getItem(this.LOCAL_STORAGE_KEY);
        return cart ? JSON.parse(cart) : [];
    }

   async getCart() {
    if (this.isLoggedIn()) {
        const userId = parseInt(localStorage.getItem('userId'));
        try {
            const response = await fetch(`/cart/${userId}`);
            if (!response.ok) {
                throw new Error('Failed to get cart');
            }
            const jsonCart = await response.json();
            localStorage.setItem(this.LOCAL_STORAGE_KEY, JSON.stringify(jsonCart));
            return jsonCart;
        } catch (error) {
            console.error('Error getting cart:', error);
        }
    }
    localStorage.removeItem(this.LOCAL_STORAGE_KEY);
    localStorage.setItem(this.LOCAL_STORAGE_KEY, JSON.stringify([]));
    return [];
}}

window.addEventListener('load', () => {
    const cartService = new CartService();
    if(cartService.isLoggedIn()) {
        cartService.getCart();
    }
    updateCartDisplay();
});

//Test set userId
window.addEventListener('load', () => {
    localStorage.setItem('userId', '8');
});