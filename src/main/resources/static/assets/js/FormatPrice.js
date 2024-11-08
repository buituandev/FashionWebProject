function formatPrices() {
    const priceElements = document.querySelectorAll('.formatted-price');

    priceElements.forEach(priceElement => {
        const priceText = priceElement.textContent;

        // Check if the price is already formatted
        if (/\d{1,3}(?:\.\d{3})*(?:,\d+)?\s₫/.test(priceText)) {
            return;
        }

        // Remove non-breaking space and currency symbol
        const cleanedPriceText = priceText.replace(/[\u00A0₫]/g, '').replace(/[^\d.]/g, '');
        const priceValue = parseFloat(cleanedPriceText);

        priceElement.textContent = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(priceValue);
    });
}

document.addEventListener("DOMContentLoaded", formatPrices);