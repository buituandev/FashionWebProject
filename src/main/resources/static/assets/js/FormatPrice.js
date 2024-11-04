function formatPrices() {
    const priceElements = document.querySelectorAll('.formatted-price');

    priceElements.forEach(priceElement => {
        const priceText = priceElement.textContent;
        console.log("Original price text:", priceText);

        // Check if the price is already formatted
        if (/\d{1,3}(?:\.\d{3})*(?:,\d+)?\s₫/.test(priceText)) {
            return;
        }

        // Remove non-breaking space and currency symbol
        const cleanedPriceText = priceText.replace(/[\u00A0₫]/g, '').replace(/[^\d.]/g, '');
        const priceValue = parseFloat(cleanedPriceText);

        const formattedPrice = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(priceValue);

        priceElement.textContent = formattedPrice;
    });
}

document.addEventListener("DOMContentLoaded", formatPrices);