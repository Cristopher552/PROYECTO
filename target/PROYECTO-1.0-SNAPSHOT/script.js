document.addEventListener("DOMContentLoaded", function () {
    let carrito = [];

    function cargarCarritoDesdeLocalStorage() {
        const carritoGuardado = JSON.parse(localStorage.getItem("carrito"));
        if (carritoGuardado) {
            carrito = carritoGuardado;
            actualizarCarrito();
        }
    }

    function agregarAlCarrito(imagen, nombre, precio) {
        let productoExistente = carrito.find(producto => producto.nombre === nombre);

        if (productoExistente) {
            productoExistente.cantidad += 1;
        } else {
            carrito.push({ imagen, nombre, precio, cantidad: 1 });
        }

        actualizarCarrito();
    }

    function actualizarCarrito() {
        const cartTableBody = document.getElementById('cartTableBody');
        const cartItemsDiv = document.getElementById('cartItems');
        cartTableBody.innerHTML = '';

        if (carrito.length === 0) {
            cartItemsDiv.style.display = 'none';
            document.getElementById('emptyCartMessage').style.display = 'block';
            cartTableBody.innerHTML = '<tr><td colspan="4" class="text-center text-muted">El carrito está vacío.</td></tr>';
            document.getElementById('cartTotal').textContent = '$0.00';
            guardarCarritoEnLocalStorage();
            return;
        } else {
            cartItemsDiv.style.display = 'block';
            document.getElementById('emptyCartMessage').style.display = 'none';
        }

        let total = 0;
        carrito.forEach((producto, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><img src="${producto.imagen}" alt="${producto.nombre}" width="50"></td>
                <td>${producto.nombre}</td>
                <td>
                    <button class="btn btn-sm btn-outline-secondary" onclick="decrementQuantity(event, ${index})">-</button>
                    <span class="mx-2">${producto.cantidad}</span>
                    <button class="btn btn-sm btn-outline-secondary" onclick="incrementQuantity(event, ${index})">+</button>
                    <button class="btn btn-sm btn-danger ms-2" onclick="removeFromCart(event, ${index})">
                        <i class="fas fa-times"></i>
                    </button>
                </td>
                <td>$${(producto.precio * producto.cantidad).toFixed(2)}</td>
            `;
            cartTableBody.appendChild(row);
            total += producto.precio * producto.cantidad;
        });

        document.getElementById('cartTotal').textContent = `$${total.toFixed(2)}`;
        guardarCarritoEnLocalStorage();
    }

    function guardarCarritoEnLocalStorage() {
        localStorage.setItem("carrito", JSON.stringify(carrito));
    }

    window.removeFromCart = function(index) {
        event.stopPropagation();
        carrito.splice(index, 1);
        actualizarCarrito();
    };

    window.incrementQuantity = function (event, index) {
        event.stopPropagation();
        carrito[index].cantidad += 1;
        actualizarCarrito();
    };

    window.decrementQuantity = function (event, index) {
        event.stopPropagation();
        if (carrito[index].cantidad > 1) {
            carrito[index].cantidad -= 1;
            actualizarCarrito();
        }
    };

    document.querySelectorAll('.add-to-cart').forEach(botonAgregar => {
        const card = botonAgregar.closest('.card');
        const imagen = card.querySelector('img').src;
        const nombre = card.querySelector('.card-title').innerText;
        const precio = parseFloat(card.querySelector('.card-text').innerText.replace('$', ''));

        botonAgregar.addEventListener('click', function () {
            agregarAlCarrito(imagen, nombre, precio);
        });
    });

    cargarCarritoDesdeLocalStorage();
});


