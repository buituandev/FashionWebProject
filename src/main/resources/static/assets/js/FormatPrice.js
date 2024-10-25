function formatPrices() {
    const priceElements = document.querySelectorAll('.formatted-price');
    priceElements.forEach(priceElement => {
        const priceText = priceElement.textContent;
        const priceValue = parseFloat(priceText.replace(/\D/g, ''));
        priceElement.textContent = priceValue.toLocaleString('vi-VN') + ' VND';
    });
}

document.addEventListener("DOMContentLoaded", formatPrices);