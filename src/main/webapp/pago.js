document.addEventListener("DOMContentLoaded", function () {
    const metodoPagoSelect = document.getElementById("metodoPago");
    const pagoTarjeta = document.getElementById("pagoTarjeta");
    const formPago = document.getElementById("formPago");
    const totalPagarElemento = document.getElementById("totalPagar");

    // Muestra u oculta el formulario de pago con tarjeta
    metodoPagoSelect.addEventListener("change", function () {
        if (metodoPagoSelect.value === "tarjeta") {
            pagoTarjeta.style.display = "block";
        } else {
            pagoTarjeta.style.display = "none";
        }
    });

    // Recuperar el total del carrito desde el localStorage
    const totalCarrito = localStorage.getItem("totalCarrito");

    if (totalCarrito) {
        totalPagarElemento.innerText = `$${totalCarrito}`;
    } else {
        totalPagarElemento.innerText = "$0.00"; // Valor por defecto si no se encuentra el total
    }

    // Recuperar los productos del carrito
    const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
    const productosCarrito = document.getElementById("productosCarrito");
    let total = 0;

    carrito.forEach(producto => {
        const row = document.createElement("tr");
        row.innerHTML = 
            `<td>${producto.nombre}</td>
            <td><span class="cantidad">${producto.cantidad}</span></td>
            <td class="precio">$${(producto.precio * producto.cantidad).toFixed(2)}</td>`;
        productosCarrito.appendChild(row);
        total += producto.precio * producto.cantidad;
    });

    totalPagarElemento.textContent = `$${total.toFixed(2)}`;

    // Función para actualizar el total (aunque la cantidad no es editable)
    function actualizarTotal() {
        total = 0;

        // Sumar el total de los productos
        carrito.forEach(producto => {
            total += producto.precio * producto.cantidad;
        });

        totalPagarElemento.textContent = `$${total.toFixed(2)}`;
        document.getElementById("total").value = total.toFixed(2);
    }

    // Inicializa el formulario de tarjeta al cargar la página
    mostrarFormularioTarjeta();

    // Lógica para manejar el envío del formulario
    formPago.addEventListener("submit", function (event) {
        event.preventDefault(); 
        
        // Captura los productos del carrito
        const nombresProductos = [];
        const cantidades = [];
        const precios = [];

        carrito.forEach(producto => {
            nombresProductos.push(producto.nombre);
            cantidades.push(producto.cantidad);
            precios.push((producto.precio * producto.cantidad).toFixed(2));
        });

        // Asigna los valores a los campos ocultos
        document.getElementById("nombresProductos").value = JSON.stringify(nombresProductos);
        document.getElementById("cantidades").value = JSON.stringify(cantidades);
        document.getElementById("precios").value = JSON.stringify(precios);

        // Envía el formulario
        formPago.submit();
    });

    // Función para mostrar u ocultar el formulario de pago con tarjeta
    function mostrarFormularioTarjeta() {
        if (metodoPagoSelect.value === "tarjeta") {
            pagoTarjeta.style.display = "block";
        } else {
            pagoTarjeta.style.display = "none";
        }
    }

    // Llama a esta función para inicializar el total cuando se carga la página
    actualizarTotal();
});
