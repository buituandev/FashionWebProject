document.addEventListener('DOMContentLoaded', function () {
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
            addItemToCart(product, 1, product.stock);
        });
    });

    updateCartDisplay();
});

document.addEventListener('DOMContentLoaded', function () {
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
            addItemToCart(product, quantity, product.stock);
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