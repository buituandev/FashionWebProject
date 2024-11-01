function formatPrices() {
    console.log("formatPrices function called");
    const priceElements = document.querySelectorAll('.formatted-price');
    console.log("Number of price elements found:", priceElements.length);

    priceElements.forEach(priceElement => {
        const priceText = priceElement.textContent;
        console.log("Original price text:", priceText);

        const priceValue = parseFloat(priceText.replace(/[^\d.]/g, ''));
        console.log("Parsed price value:", priceValue);

        const formattedPrice = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(priceValue);
        console.log("Formatted price:", formattedPrice);

        priceElement.textContent = formattedPrice;
    });
}

document.addEventListener("DOMContentLoaded", formatPrices);