 const videos = [
            {
                title: "Es Sinom",
                description: "Varian spesial Es Sinom",
                source: "video/1.mp4",
                poster: "img/4.png"
            }
        ];

        let cart = [];

        function renderProducts() {
    const container = document.getElementById('product-container');
    container.innerHTML = '';
    
    products.forEach(product => {
        const card = document.createElement('div');
        card.classList.add('product-card');
        card.innerHTML = `
            <img src="${product.image}" alt="${product.name}">
            <h3>${product.name}</h3> 
            <p>${product.description}</p>
            <label class="size-label">Pilih Ukuran:</label>
            <select class="size-selector" data-product-id="${product.id}">
                ${product.sizes.map(size => 
                    `<option value="${size.size}">Ukuran ${size.size} oz - Rp ${size.price.toLocaleString()}</option>`
                ).join('')}
            </select>
            <div class="price" id="price-${product.id}">Rp ${product.sizes[0].price.toLocaleString()}</div>
            <label class="quantity-label">Jumlah:</label>
            <input type="number" class="quantity-input" data-product-id="${product.id}" value="1" min="1"> <!-- Tambahkan input kuantitas -->
            <button onclick="addToCart(${product.id})" class="cta-button shake">Tambah ke Keranjang</button>
           
        `;
        card.querySelector('.size-selector').addEventListener('change', function() {
            updatePrice(this);
        });
        
        container.appendChild(card);
    });
}
                

        function renderVideos() {
            const container = document.getElementById('video-container');
            container.innerHTML = '';
            
            videos.forEach(video => {
                const videoItem = document.createElement('div');
                videoItem.classList.add('video-item');
                videoItem.innerHTML = `
                    <video controls poster="${video.poster}">
                        <source src="${video.source}" type="video/mp4">
                        Browser Anda tidak mendukung tag video.
                    </video>
                    <div class="video-info">
                        <h3>${video.title}</h3>
                        <p>${video.description}</p>
                    </div>
                `;
                
                container.appendChild(videoItem);
            });
        }

        function addToCart(productId) {
    const productSelect = document.querySelector(`.size-selector[data-product-id="${productId}"]`);
    const quantityInput = document.querySelector(`.quantity-input[data-product-id="${productId}"]`);
    const selectedSize = parseInt(productSelect.value);
    const quantity = parseInt(quantityInput.value);
    const product = products.find(p => p.id === productId);
    const selectedSizeObj = product.sizes.find(s => s.size === selectedSize);

    // Check if product already in cart
    const existingCartItem = cart.find(item => 
        item.id === productId && item.size === selectedSize
    );

    if (existingCartItem) {
        existingCartItem.quantity += quantity;
    } else {
        cart.push({
            id: productId,
            name: product.name,
            size: selectedSize,
            price: selectedSizeObj.price,
            quantity: quantity,
            image: product.image // Tambahkan properti gambar
        });
    }

    updateCartDisplay();
}

function updateCartDisplay() {
    const cartItemsContainer = document.getElementById('cart-items');
    const cartTotalContainer = document.getElementById('cart-total');

    // Clear previous cart items
    cartItemsContainer.innerHTML = '';

    // Populate cart items
    cart.forEach(item => {
        const cartItemDiv = document.createElement('div');
        cartItemDiv.classList.add('cart-item');
        cartItemDiv.innerHTML = `
            <img src="${item.image}" alt="${item.name}" style="width: 50px; height: 50px; margin-right: 10px;"> <!-- Tambahkan gambar -->
            <span>${item.name} (${item.size} oz)</span>
            <span>Rp ${item.price.toLocaleString()} x ${item.quantity}</span>
            <button onclick="removeFromCart(${item.id}, ${item.size})">Hapus</button>
        `;
        cartItemsContainer.appendChild(cartItemDiv);
    });

    // Calculate and display total
    const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    cartTotalContainer.innerHTML = `Total: Rp ${total.toLocaleString()}`;
}
// ... existing code ...
function toggleCart() {
        const cartModal = document.getElementById('cart-modal');
        const cartButton = document.querySelector('.cart-button');

        // Tambahkan efek getaran
        cartButton.classList.add('shake');

        // Hapus kelas shake setelah animasi selesai
        setTimeout(() => {
            cartButton.classList.remove('shake');
        }, 500);

        cartModal.style.display = cartModal.style.display === 'flex' ? 'none' : 'flex';
    }
        
    
        function removeFromCart(productId, size) {
            const index = cart.findIndex(item => 
                item.id === productId && item.size === size
            );

            if (index !== -1) {
                if (cart[index].quantity > 1) {
                    cart[index].quantity -= 1;
                } else {
                    cart.splice(index, 1);
                }
            }

            updateCartDisplay();
        }

        function toggleCart() {
            const cartModal = document.getElementById('cart-modal');
            cartModal.style.display = cartModal.style.display === 'flex' ? 'none' : 'flex';
        }

        function checkout() {
            if (cart.length === 0) {
        alert('Keranjang Anda kosong!');
        return;
    }

    // Create WhatsApp message with order details
    const orderDetails = cart.map(item => 
        `${item.name} (${item.size} oz) x ${item.quantity}: Rp ${(item.price * item.quantity).toLocaleString()}`
    ).join('%0A');

    const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const whatsappMessage = `Halo, saya ingin memesan:%0A${orderDetails}%0ATotal: Rp ${total.toLocaleString()}`;

    // Open WhatsApp with pre-filled message
    window.open(`https://wa.me/6282199205744?text=${whatsappMessage}`, '_blank');

    // Clear cart after checkout
    cart = [];
    updateCartDisplay();
    toggleCart();
        }

        // Initialize the page
        document.addEventListener('DOMContentLoaded', () => {
            renderProducts();
            renderVideos();
        });
    